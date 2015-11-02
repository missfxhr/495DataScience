package source;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.mapred.lib.db.*;

public class Postgresql2MR {

    // Java class for encapsulating data rows read from database
    public static class LocationRecord implements Writable, DBWritable {
        String id;             //location_id column
        int class_code;        //class_code column

        public LocationRecord() {

        }

        //override necessary interface methods
        public void readFields(DataInput in) throws IOException {
            this.id = Text.readString(in);
            this.class_code = in.readInt();
        }

        public void write(PreparedStatement stmt) throws SQLException {
            stmt.setString(1, this.id);
            stmt.setInt(2, this.class_code);
        }

        public void readFields(ResultSet result) throws SQLException {
            this.id = result.getString(1);
            this.class_code = result.getInt(2);
        }

        public void write(DataOutput out) throws IOException {
            Text.writeString(out, this.id);
            out.writeInt(this.class_code);
        }

        public String toString() {
            return new String(this.id + " " + this.class_code);
        }

    }

    //mapper
    public static class MyMapper extends MapReduceBase implements
            Mapper<LongWritable, LocationRecord, IntWritable, IntWritable> {
        private static final IntWritable one = new IntWritable(1);
        private IntWritable intermediate_key = new IntWritable();
        public void map(LongWritable key, LocationRecord value,
                        OutputCollector<IntWritable, IntWritable> collector, Reporter reporter) throws IOException {
            //using class_code as intermediate key
            intermediate_key.set(value.class_code);
            //put entry<class_code,1> into context
            collector.collect(intermediate_key, one);
        }
    }

    //reducer
    public static class MyReducer extends MapReduceBase implements
            Reducer<IntWritable, IntWritable, IntWritable, IntWritable> {
        public void reduce(IntWritable key, Iterator<IntWritable> values,
                           OutputCollector<IntWritable, IntWritable> output, Reporter reporter) throws IOException {
            //calculate the sum of each class_code entries
            int sum = 0;
            while(values.hasNext()) {
                sum += values.next().get();
            }
            //put entry<class_code,sum> into context
            output.collect(key,new IntWritable(sum));
        }
    }

    public static void main(String[] args) throws IOException {
        //new job configuration
        JobConf conf = new JobConf(Postgresql2MR.class);
        //set reducer's output data type
        conf.setOutputKeyClass(IntWritable.class);
        conf.setOutputValueClass(IntWritable.class);
        //set mapper's input source
        conf.setInputFormat(DBInputFormat.class);
        //set output path
        Path outputPath = new Path("Postgresql2MR_output");
        FileSystem.get(conf).delete(outputPath,true);
        FileOutputFormat.setOutputPath(conf, outputPath);
        //set database source
        DBConfiguration.configureDB(conf, "org.postgresql.Driver", "jdbc:postgresql://datascience495.coxgjlcsyyhn.us-east-1.rds.amazonaws.com:5432/datascience495","postgresql","12345678");
        //set Java's API for database
        String[] fields = {"location_id", "class_code"};
        DBInputFormat.setInput(conf, LocationRecord.class,"locations",null,"location_id", fields);
        //specify customized mapper and reducer
        conf.setMapperClass(MyMapper.class);
        conf.setReducerClass(MyReducer.class);
        //run job
        JobClient.runJob(conf);
    }
}