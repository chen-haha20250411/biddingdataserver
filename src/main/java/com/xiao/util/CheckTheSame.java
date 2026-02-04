package com.xiao.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.Vector;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class CheckTheSame {
    /**
     * 分词
     * @author: administrator
     * @Date: 2016年3月5日15:10:47
     * @param str
     * @return
     */
    public static Vector<String> participle( String str ) {

        Vector<String> str1 = new Vector<String>() ;//对输入进行分词

        try {

            StringReader reader = new StringReader( str );
            IKSegmenter ik = new IKSegmenter(reader,false);//当为true时，分词器进行智能切分
            Lexeme lexeme = null ;

            while( ( lexeme = ik.next() ) != null ) {
                str1.add( lexeme.getLexemeText() );
            }

            if( str1.size() == 0 ) {
                return null ;
            }

            //分词后
            // System.out.println( "str分词后：" + str1 );

        } catch ( IOException e1 ) {
            //System.out.println();
        }
        return str1;
    }
    /**
     * 返回比较的两个字符串的相似度
     * @param strone
     * @param strtwo
     * @return
     */
    public String getSemblance(String strone,String strtwo) {
        String semblanceString = "0.0000";
        //分词
        Vector<String> strs1 = participle(strone) ;
        Vector<String> strs2 = participle(strtwo) ;
        //根据分词返回相似度
        double same = 0 ;
        try {
            same = IKAnalyzerUtil.getSimilarity( strs1 , strs2 );
        } catch (Exception e) {
            //System.out.println( e.getMessage() );
        }
        semblanceString=String.valueOf(same);
        //System.out.println( "相似度：" + same );
        return semblanceString;
    }
    public static void main(String[] args) {

        //分词
        Vector<String> strs1 = participle( "化妆品用甲基苯基硅油和纳米硅树脂粉制" ) ;
        Vector<String> strs2 = participle( "纳米硅甲基苯基硅油化妆品" ) ;

        //根据分词返回相似度
        double same = 0 ;
        try {
            same = IKAnalyzerUtil.getSimilarity( strs1 , strs2 );
        } catch (Exception e) {
            System.out.println( e.getMessage() );
        }

        System.out.println( "相似度1：" + same );
    }
}
