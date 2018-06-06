package com.steven.search;

import com.chenlb.mmseg4j.analysis.MMSegAnalyzer;
import com.steven.resp.AppConfig;
import com.steven.resp.MusicInfo;
import javafx.util.Duration;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.*;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.NumericUtils;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.util.*;

import static org.apache.lucene.index.IndexWriterConfig.OpenMode.CREATE_OR_APPEND;

public class MusicSearcher {
    private IndexWriter writer;
    private IndexReader reader;
    private Analyzer analyzer;
    private Directory directory;
    private boolean hasDelete = false;

    private static MusicSearcher INSTANCE = new MusicSearcher();

    public MusicSearcher() {

    }

    public void prepare() {
        if (analyzer != null) {
            return;
        }

        try{
            this.analyzer = new MMSegAnalyzer();
            this.directory = FSDirectory.open(AppConfig.getAppIndex());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    IndexSearcher getSearcher() {
        IndexSearcher searcher = null;
        try {
            if (reader == null) {
                this.reader = IndexReader.open(directory);
            }else {
                IndexReader ir = IndexReader.openIfChanged(reader);
                if (ir != null) {
                    reader.close();
                    reader = ir;
                }
            }
            searcher = new IndexSearcher(reader);
        } catch (CorruptIndexException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return searcher;
    }

    IndexWriter getWriter() {
        try {
            if (writer == null) {
                IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_36, this.analyzer);
                iwc.setOpenMode(CREATE_OR_APPEND);
                iwc.setMaxBufferedDocs(500);
                this.writer = new IndexWriter(directory, iwc);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        return writer;
    }

    public void addMusic(MusicInfo info) {
        Document doc = toDocument(info);
        try {
            getWriter().addDocument(doc);

        } catch (CorruptIndexException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            commit();
        }
    }

    public void addMusics(Collection<MusicInfo> col){
        try{
            for(MusicInfo info : col){
                getWriter().addDocument(toDocument(info));
            }
        }catch(CorruptIndexException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            commit();
        }
    }

    public void deleteMusic(MusicInfo info) {
        try {
            getWriter().deleteDocuments(new Term("id", NumericUtils.intToPrefixCoded(info.hasCode())));
        } catch (CorruptIndexException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            commit();
        }
    }

    public void deleteMusicByGroup(String group) {
        try {
            getWriter().deleteDocuments(new Term("group", group));
        } catch (CorruptIndexException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            commit();
        }
    }

    public void cleanTrash() {
        try {
            if (hasDelete) {
                prepare();
                getWriter().forceMergeDeletes();
            }
        } catch (CorruptIndexException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, List<MusicInfo>> search(String word) {
        AppConfig.debug("search = " + word);
        HashMap<String, List<MusicInfo>> result = new HashMap<String, List<MusicInfo>>();
        IndexSearcher searcher = null;
        try {
            QueryParser parser = new MultiFieldQueryParser(Version.LUCENE_36, new String[]{"singer", "name"}, analyzer);
            parser.setPhraseSlop(6);
            parser.setDefaultOperator(QueryParser.Operator.AND);
            Query query = parser.parse(word);

            searcher = getSearcher();
            Filter filter = null;
            TopDocs topDocs = searcher.search(query, filter, Integer.MAX_VALUE);

            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                int docsn = scoreDoc.doc;
                Document doc = searcher.doc(docsn);
                MusicInfo tm = toMusicInfo(doc);
                if (result.get(tm.group) == null) {
                    result.put(tm.group, new LinkedList<MusicInfo>());
                }

                result.get(tm.group).add(tm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(searcher!=null)
                try {
                    searcher.close();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        return result;
    }

    public void close(){
        if(analyzer != null){
            analyzer.close();
        }

        try{
            if(writer != null) writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }

        try{
            if(reader != null) reader.close();
        }catch(IOException e){
            e.printStackTrace();
        }

        try{
            if(directory != null) directory.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    MusicInfo toMusicInfo(Document doc){
        MusicInfo result = new MusicInfo();
        result.group = doc.get("group");
        result.url = doc.get("url");
        result.time = Duration.valueOf(doc.get("duration"));

        return result;
    }

    private void commit() {
        try {
            getWriter().commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Document toDocument(MusicInfo info) {
        Document doc = new Document();
        doc.add(new Field("id", NumericUtils.intToPrefixCoded(info.hasCode()), Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));

        doc.add(new Field("singer", info.getSinger(), Field.Store.NO, Field.Index.ANALYZED));
        doc.add(new Field("name", info.getName(), Field.Store.NO, Field.Index.ANALYZED));

        doc.add(new Field("group", info.group, Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
        doc.add(new Field("url", info.url, Field.Store.YES, Field.Index.NO));

        return doc;

    }

    public static MusicSearcher getInstance(){
        return INSTANCE;
    }
}
