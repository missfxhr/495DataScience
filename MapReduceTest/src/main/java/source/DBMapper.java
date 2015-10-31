//package source;
//
//import org.apache.hadoop.io.LongWritable;
//import org.apache.hadoop.io.Text;
//import org.apache.hadoop.mapred.MapReduceBase;
//import org.apache.hadoop.mapred.OutputCollector;
//import org.apache.hadoop.mapred.Reporter;
//
//import java.io.IOException;
//
//public class DBMapper extends MapReduceBase implements Mapper<LongWritable, CourceRecord, LongWritable, Text> {
//    public void map(LongWritable key, LocationRecorder value, OutputCollector<LongWritable, Text> collector, Reporter reporter) throws IOException {
//        collector.collect(new LongWritable(value.id), new Text(value.toString()));
//    }
//}