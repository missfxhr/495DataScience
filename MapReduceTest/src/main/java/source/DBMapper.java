package source;

public class DBMapper extends MapReduceBase implements Mapper<LongWritable, CourceRecord, LongWritable, Text> {
    public void map(LongWritable key, LocationRecorder value, OutputCollector<LongWritable, Text> collector, Reporter reporter) throws IOException {
        collector.collect(new LongWritable(value.id), new Text(value.toString()));
    }
}