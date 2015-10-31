package source;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.mapred.lib.IdentityReducer;
import org.apache.hadoop.mapred.lib.db.DBConfiguration;
import org.apache.hadoop.mapred.lib.db.DBInputFormat;
import org.apache.hadoop.mapred.lib.db.DBOutputFormat;
import org.apache.hadoop.mapred.lib.db.DBWritable;

/**
 * Function: ���� mr �� mysql �����ݽ������˲���������һ�����е����ݸ��Ƶ���һ�ű���
 *           ʵ�ʵ��У�����ֻ��Ҫ�� mysql ��������д�� mysql �С�
 * date: 2013-7-29 ����2:34:04 <br/>
 * @author june
 */
public class Postgresql2MR {
    // DROP TABLE IF EXISTS `hadoop`.`studentinfo`;
    // CREATE TABLE studentinfo (
    // id INTEGER NOT NULL PRIMARY KEY,
    // name VARCHAR(32) NOT NULL);

    public static class LocationRecord implements Writable, DBWritable {
        String id;
        int class_code;

        public LocationRecord() {

        }

        public void readFields(DataInput in) throws IOException {
            this.id = Text.readString(in);
            this.class_code = in.readInt();
        }

        public String toString() {
            return new String(this.id + " " + this.class_code);
        }

        public void write(PreparedStatement stmt) throws SQLException {
            stmt.setString(1, this.id);
            stmt.setInt(11, this.class_code);
        }

        public void readFields(ResultSet result) throws SQLException {
            this.id = result.getString(1);
            this.class_code = result.getInt(11);
        }

        public void write(DataOutput out) throws IOException {
            Text.writeString(out, this.id);
            out.writeInt(this.class_code);
        }
    }

    // ��ס�˴��Ǿ�̬�ڲ��࣬Ҫ��Ȼ���Լ�ʵ���޲ι����������ߵ������쳣��
    // Caused by: java.lang.NoSuchMethodException: DBInputMapper.<init>()
    // http://stackoverflow.com/questions/7154125/custom-mapreduce-input-format-cant-find-constructor
    // �����Բ�ʽ��ת����û����һ��д�Եġ�����
    public static class DBInputMapper extends MapReduceBase implements
            Mapper<LongWritable, LocationRecord, LongWritable, Text> {
        public void map(LongWritable key, LocationRecord value,
                        OutputCollector<LongWritable, Text> collector, Reporter reporter) throws IOException {
            collector.collect(new LongWritable(value.class_code), new Text(value.toString()));
        }
    }

    public static class MyReducer extends MapReduceBase implements
            Reducer<LongWritable, Text, LocationRecord, Text> {
        public void reduce(LongWritable key, Iterator<Text> values,
                           OutputCollector<LocationRecord, Text> output, Reporter reporter) throws IOException {
            String[] splits = values.next().toString().split(" ");
            LocationRecord r = new LocationRecord();
            r.id = splits[0];
            r.class_code = Integer.parseInt(splits[1]);
            output.collect(r, new Text(Integer.toString(r.class_code)));
        }
    }

//    public static void main(String[] args) throws IOException {
//        JobConf conf = new JobConf(Postgresql2MR.class);
//        // DistributedCache.addFileToClassPath(new Path("/tmp/mysql-connector-java-5.0.8-bin.jar"), conf);
//
//        conf.setMapOutputKeyClass(LongWritable.class);
//        conf.setMapOutputValueClass(Text.class);
//        conf.setOutputKeyClass(LongWritable.class);
//        conf.setOutputValueClass(Text.class);
//
//        conf.setOutputFormat(DBOutputFormat.class);
//        conf.setInputFormat(DBInputFormat.class);
//        // // mysql to hdfs
//        // conf.setReducerClass(IdentityReducer.class);
//        // Path outPath = new Path("/tmp/1");
//        // FileSystem.get(conf).delete(outPath, true);
//        // FileOutputFormat.setOutputPath(conf, outPath);
//
//        DBConfiguration.configureDB(conf, "org.postgresql.Driver", "jdbc:postgresql:datascience495.coxgjlcsyyhn.us-east-1.rds.amazonaws.com:5432/datascience495",
//                "postgresql", "12345678");
//        String[] fields = { "id", "class_code"};
//        // �� t �������
//        DBInputFormat.setInput(conf, LocationRecord.class, "locations", null, "id", fields);
//        // mapreduce ����������� t2 ��
//        DBOutputFormat.setOutput(conf, "t2", "id", "class_code");
//        // conf.setMapperClass(org.apache.hadoop.mapred.lib.IdentityMapper.class);
//        conf.setMapperClass(DBInputMapper.class);
//        conf.setReducerClass(MyReducer.class);
//
//        JobClient.runJob(conf);
//    }
    public static void main(String[] args) throws IOException {
        JobConf conf = new JobConf(Postgresql2MR.class);
        conf.setOutputKeyClass(LongWritable.class);
        conf.setOutputValueClass(Text.class);
        conf.setInputFormat(DBInputFormat.class);
        FileOutputFormat.setOutputPath(conf, new Path("~/Postgresql2MR"));
        DBConfiguration.configureDB(conf, "org.postgresql.Driver", "jdbc:postgresql:datascience495.coxgjlcsyyhn.us-east-1.rds.amazonaws.com:5432/datascience495","postgresql", "12345678");
        String[] fields = {"id", "class_code"};
        DBInputFormat.setInput(conf, LocationRecord.class,"locations",null, "id", fields);
        conf.setMapperClass(DBInputMapper.class);
        conf.setReducerClass(IdentityReducer.class);
        JobClient.runJob(conf);
    }
}