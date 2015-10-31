package source;

import java.io.IOException;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;


import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.lib.IdentityReducer;
import org.apache.hadoop.mapred.lib.db.DBConfiguration;
import org.apache.hadoop.mapred.lib.db.DBInputFormat;

import org.postgresql.Driver;


public class DB {
    public static void main(String[] args) throws IOException {
        String[] argc = {"jdbc:postgresql:datascience495.coxgjlcsyyhn.us-east-1.rds.amazonaws.com:5432", "postgresql", "12345678"};

        try {
            JobConf conf = new JobConf(DB.class);
            Class.forName("com.mysql.jdbc.Driver");
            DBConfiguration.configureDB(conf, "com.mysql.jdbc.Driver", argc[0], argc[1], argc[2]);
            String[] fields = {"id"};
            DBInputFormat.setInput(conf, CourceRecord.class, "tb", null, "id", fields);

            conf.setInputFormat(DBInputFormat.class);
            conf.setOutputKeyClass(LongWritable.class);
            conf.setOutputValueClass(Text.class);


            Path path = new Path("DBOUTPUT");
            FileOutputFormat.setOutputPath(conf, path);

            conf.setMapperClass(DBMapper.class);
            conf.setReducerClass(IdentityReducer.class);
            //如果文件存在则删除
            FileSystem hdfs = path.getFileSystem(conf);
            if (hdfs.exists(path)) {
                hdfs.delete(path, true);
            }

            JobClient.runJob(conf);
        } catch (ClassNotFoundException e) {
            System.err.println("mysql.jdbc.Driver not found");
        }
    }
}