package org.anc.grid.pipeline;

import org.junit.*;

import static org.junit.Assert.*;

import org.lappsgrid.api.Data;
import org.lappsgrid.api.WebService;
import org.lappsgrid.core.DataFactory;
import org.lappsgrid.discriminator.*;

import org.anc.lapps.pipeline.Pipeline;

public class PipelineTest
{
   @Test
   public void testRequires()
   {

   }

   @Test
   public void testEmptyPipeline()
   {
      Pipeline pipeline = new Pipeline();
      assertFalse(pipeline.validate());
   }

   @Test
   public void testGateAll()
   {
      System.out.println("PipelineTest.testGateAll");
      Pipeline pipeline = new Pipeline();
      pipeline.add(new GateSplitter());
      pipeline.add(new GateTokenizer());
      pipeline.add(new GateTagger());
      assertTrue(pipeline.validate());
      System.out.println("Passed.");
   }

   @Test
   public void testInvalidGate1()
   {
      System.out.println("PipelineTest.testInvalidGate1");
      Pipeline pipeline = new Pipeline();
      pipeline.add(new GateSplitter());
      pipeline.add(new GateTagger());
      assertFalse(pipeline.validate());
      System.out.println("Passed.");
   }

   @Test
   public void testOpenNlp()
   {
      System.out.println("PipelineTest.testOpenNlp");
      Pipeline pipeline = new Pipeline();
      pipeline.add(new OpenNLPSplitter());
      pipeline.add(new OpenNLPTokenizer());
      pipeline.add(new OpenNLPTagger());
      assertTrue(pipeline.validate());
      System.out.println("Passed.");
   }

   @Test
   public void testStanford()
   {
      System.out.println("PipelineTest.testStanford");
      Pipeline pipeline = new Pipeline();
      pipeline.add(new StanfordSplitter());
      pipeline.add(new StanfordTokenizer());
      pipeline.add(new StanfordTagger());
      assertTrue(pipeline.validate());
      System.out.println("Passed.");
   }

   @Test
   public void testInvalidMix()
   {
      System.out.println("PipelineTest.testInvalidMix");
      Pipeline pipeline = new Pipeline();
      pipeline.add(new GateSplitter());
      pipeline.add(new OpenNLPTokenizer());
      pipeline.add(new StanfordTagger());
      assertFalse(pipeline.validate());
      System.out.println("Passed.");
   }
}

abstract class AbstractService implements WebService
{
   @Override
   public Data execute(Data input)
   {
      return DataFactory.error("Unsupported operation.");
   }

   @Override
   public Data configure(Data config)
   {
      return DataFactory.ok();
   }
}

class GateSplitter extends AbstractService
{
   @Override
   public long[] produces()
   {
      return new long[]{Types.GATE, Types.SENTENCE};
   }

   @Override
   public long[] requires()
   {
      return new long[]{Types.TEXT};
   }
}

class GateTokenizer extends AbstractService
{
   @Override
   public long[] requires()
   {
      return new long[]{Types.GATE, Types.SENTENCE};
   }

   @Override
   public long[] produces()
   {
      return new long[]{Types.GATE, Types.SENTENCE, Types.TOKEN};
   }
}

class GateTagger extends AbstractService
{
   @Override
   public long[] requires()
   {
      return new long[]{Types.GATE, Types.SENTENCE, Types.TOKEN};
   }

   @Override
   public long[] produces()
   {
      return new long[]{Types.GATE, Types.SENTENCE, Types.TOKEN, Types.POS};

   }
}

class StanfordSplitter extends AbstractService
{

   @Override
   public long[] requires()
   {
      return new long[]{Types.TEXT};
   }

   @Override
   public long[] produces()
   {
      return new long[]{Types.STANFORD, Types.SENTENCE};
   }
}

class StanfordTokenizer extends AbstractService
{
   @Override
   public long[] requires()
   {
      return new long[]{Types.STANFORD, Types.SENTENCE};
   }

   @Override
   public long[] produces()
   {
      return new long[]{Types.STANFORD, Types.SENTENCE, Types.TOKEN};
   }
}

class StanfordTagger extends AbstractService
{
   @Override
   public long[] requires()
   {
      return new long[]{Types.STANFORD, Types.SENTENCE};
   }

   @Override
   public long[] produces()
   {
      return new long[]{Types.STANFORD, Types.SENTENCE, Types.TOKEN, Types.POS};
   }
}

class OpenNLPSplitter extends AbstractService
{

   @Override
   public long[] requires()
   {
      return new long[]{Types.TEXT};
   }

   @Override
   public long[] produces()
   {
      return new long[]{Types.OPENNLP, Types.SENTENCE};
   }
}

class OpenNLPTokenizer extends AbstractService
{
   @Override
   public long[] requires()
   {
      return new long[]{Types.OPENNLP, Types.SENTENCE};
   }

   @Override
   public long[] produces()
   {
      return new long[]{Types.OPENNLP, Types.SENTENCE, Types.TOKEN};
   }
}

class OpenNLPTagger extends AbstractService
{
   @Override
   public long[] requires()
   {
      return new long[]{Types.OPENNLP, Types.SENTENCE};
   }

   @Override
   public long[] produces()
   {
      return new long[]{Types.OPENNLP, Types.SENTENCE, Types.TOKEN, Types.POS};
   }
}