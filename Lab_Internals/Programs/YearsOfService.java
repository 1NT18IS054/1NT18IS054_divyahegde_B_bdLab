package internal.mypack.two;

import java.io.IOException;
import java.util.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;
	
public class YearsOfService{

//MAPPER CODE	
	   
public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {

private Text word = new Text("Cumulative Years of Service : ");

public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
	String myvalue = value.toString();
	String[] Tokens = myvalue.split(",");
	output.collect(word,new IntWritable(Integer.parseInt(Tokens[1])));
	
}
}

//REDUCER CODE	
public static class Reduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {
public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {  
	
	int count=0;
	while(values.hasNext())
	{
		count+=values.next().get();
	
	}	
	output.collect(key,new IntWritable(count));
	}
}
	
//DRIVER CODE
public static void main(String[] args) throws Exception {
	JobConf conf = new JobConf(YearsOfService.class);
	conf.setJobName("Cumulative years of service of all employees");
	conf.setOutputKeyClass(Text.class);
	conf.setOutputValueClass(IntWritable.class);
	conf.setMapperClass(Map.class);
	conf.setCombinerClass(Reduce.class);
	conf.setReducerClass(Reduce.class);
	conf.setInputFormat(TextInputFormat.class);
	conf.setOutputFormat(TextOutputFormat.class); // hadoop jar jarname classpath inputfolder outputfolder
	FileInputFormat.setInputPaths(conf, new Path(args[0]));
	FileOutputFormat.setOutputPath(conf, new Path(args[1]));
	JobClient.runJob(conf);   
}
}

