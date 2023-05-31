package com.pucpr.atpbigdata;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Informacao5 {
    
    public static class MapperInformacao5 extends Mapper<Object, Text, Text, IntWritable> {        
        @Override
        public void map(Object chave, Text valor, Context context) throws IOException, InterruptedException {
            String linha = valor.toString();
            String[] campos = linha.split(";");
            
            if((campos.length == 10) && (campos[1].equals("2016"))) {
                String mercadoria = campos[3];
                int quantidade = 1;
                
                Text chaveMap = new Text(mercadoria);
                IntWritable valorMap = new IntWritable(quantidade);
                
                context.write(chaveMap, valorMap); 
            }
        }        
    }
    
    public static class ReducerInformacao5 extends Reducer<Text, IntWritable, Text, IntWritable> {    
            @Override
            public void reduce(Text chave, Iterable<IntWritable> valores, Context context) throws IOException, InterruptedException {
                int soma = 0;
                
                for(IntWritable valor : valores){
                    soma += valor.get();
                }
   
                context.write(chave, new IntWritable(soma));
            }
    }
    
    public static void main(String[] args) throws Exception {

        String arquivoEntrada = "/home/Disciplinas/FundamentosBigData/OperacoesComerciais/base_100_mil.csv";
        String arquivoSaida = "/home2/ead2022/SEM1/cesar.franco/Desktop/Analise5/informacao5";
        
        if(args.length == 2){
            arquivoEntrada = args[0];
            arquivoSaida = args[1];
        }
        
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "implementacao5");
        
        job.setJarByClass(Informacao5.class);
        job.setMapperClass(MapperInformacao5.class);
        job.setReducerClass(ReducerInformacao5.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        
        FileInputFormat.addInputPath(job, new Path(arquivoEntrada));
        FileOutputFormat.setOutputPath(job, new Path(arquivoSaida));
        
        job.waitForCompletion(true); 
    }
}