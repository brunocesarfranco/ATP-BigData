/*
# Grupo 100: 
        - Ana Paula Barbosa da Cruz Szezech
        - Bruno Cesar de Oliveira Franco
        - Melissa Wunsch
# RA: 1112022100423
# Matéria: Fundamentos de Big Data
# Professor: Felipe Gabriel De Mello Elias
# Objetivo: É um projeto feito em Java com Maven, 
            criado a partir da IDE NetBeans (versão 8.2) 
            em uma VM com sistema operacional Linux.
# Repositório: https://github.com/brunocesarfranco/ATP-BigData
*/

package com.pucpr.atpbigdata;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Informacao7 {
    public static class MapperInformacao7 extends Mapper<Object, Text, Text, LongWritable> {        
        @Override
        public void map(Object chave, Text valor, Context context) throws IOException, InterruptedException {            
            String linha = valor.toString();
            String[] campos = linha.split(";");
            LongWritable valorMap = new LongWritable(0);

            if(campos.length == 10) {
                String mercadoria = campos[3];
                String peso = campos[6];

                Text chaveMap = new Text(mercadoria);            
            
                try 
                {
                    valorMap = new LongWritable(Long.parseLong(peso));
                } 
                catch (NumberFormatException err) {

                } finally {
                }
                    context.write(chaveMap, valorMap);
                }
            }
        }   
    
    public static class ReducerInformacao7 extends Reducer<Text, LongWritable, Text, LongWritable> {
        @Override
        public void reduce(Text chave, Iterable<LongWritable> valores, Context context) throws IOException, InterruptedException {
            int soma = 0;
            
            for(LongWritable valor : valores)
            {
                soma += valor.get();
            }
            
            LongWritable result = new LongWritable(soma);
            context.write(chave, result);
        }
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println("AnalisandoMercadoria com maior quantidade de transações financeiras... ");
        String arquivoEntrada = "/home/Disciplinas/FundamentosBigData/OperacoesComerciais/base_100_mil.csv";
        String arquivoSaida = "/home2/ead2022/SEM1/cesar.franco/Desktop/Analise7/Informacao7";
        
        if(args.length == 2)
        {
            arquivoEntrada = args[0];
            arquivoSaida = args[1];
        }
        
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "implementacao7");
        
        job.setJarByClass(Informacao7.class);
        job.setMapperClass(MapperInformacao7.class);
        job.setReducerClass(ReducerInformacao7.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);
        
        FileInputFormat.addInputPath(job, new Path(arquivoEntrada));
        FileOutputFormat.setOutputPath(job, new Path(arquivoSaida));
        
        job.waitForCompletion(true); 
    }
}