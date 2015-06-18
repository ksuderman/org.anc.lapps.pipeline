package org.anc.grid.pipeline;

import org.anc.lapps.pipeline.Pipeline;
import org.junit.Ignore;
import org.junit.Test;
import org.lappsgrid.api.WebService;
import org.lappsgrid.core.DataFactory;
import static org.lappsgrid.discriminator.Discriminators.Uri;
import org.lappsgrid.metadata.ServiceMetadata;
import org.lappsgrid.serialization.Data;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

	@Ignore
	public void testGateAll()
	{
		System.out.println("PipelineTest.testGateAll");
		Pipeline pipeline = new Pipeline();
		pipeline.add(new GateTokenizer());
		pipeline.add(new GateTagger());
		pipeline.add(new GateSplitter());
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
	public String execute(String input)
	{
		return DataFactory.error("Unsupported operation.");
	}

}

class GateSplitter extends AbstractService
{
	@Override
	public String getMetadata()
	{
		return new MetadataBuilder()
				  .requireEncoding("UTF-8")
				  .produceEncoding("UTF-8")
				  .requireFormat(Uri.GATE)
				  .produceFormat(Uri.GATE)
				  .require(Uri.TOKEN)
				  .produce(Uri.SENTENCE).build();
	}
}

class GateTokenizer extends AbstractService
{
	@Override
	public String getMetadata()
	{
		return new MetadataBuilder()
				  .requireEncoding("UTF-8")
				  .produceEncoding("UTF-8")
				  .requireFormat(Uri.TEXT)
				  .requireFormat(Uri.XML)
				  .requireFormat(Uri.GATE)
				  .produceFormat(Uri.GATE)
				  .produce(Uri.TOKEN)
				  .build();
	}
}

class GateTagger extends AbstractService
{
	@Override
	public String getMetadata()
	{
		return new MetadataBuilder()
				  .requireEncoding("UTF-8")
				  .produceEncoding("UTF-8")
				  .produce(Uri.POS)
				  .require(Uri.TOKEN)
				  .produceFormat(Uri.GATE)
				  .requireFormat(Uri.GATE)
				  .build();
	}
}

class StanfordSplitter extends AbstractService
{

	@Override
	public String getMetadata()
	{
		return new MetadataBuilder()
				  .produce(Uri.SENTENCE)
				  .require(Uri.TOKEN)
				  .produceFormat(Uri.LAPPS)
				  .requireFormat(Uri.LAPPS)
				  .build();
	}
}

class StanfordTokenizer extends AbstractService
{
	@Override
	public String getMetadata()
	{
		return new MetadataBuilder()
				  .produce(Uri.TOKEN)
				  .requireFormat(Uri.TEXT)
				  .requireFormat(Uri.LAPPS)
				  .produceFormat(Uri.LAPPS)
				  .build();
	}
}

class StanfordTagger extends AbstractService
{
	@Override
	public String getMetadata()
	{
		return new MetadataBuilder()
				  .produce(Uri.POS)
				  .require(Uri.TOKEN)
				  .produceFormat(Uri.LAPPS)
				  .requireFormat(Uri.LAPPS)
				  .build();
	}
}

class OpenNLPSplitter extends AbstractService
{
	@Override
	public String getMetadata()
	{
		return new MetadataBuilder()
				  .produce(Uri.SENTENCE)
				  .require(Uri.TOKEN)
				  .produceFormat(Uri.LAPPS)
				  .requireFormat(Uri.LAPPS)
				  .build();
	}
}

class OpenNLPTokenizer extends AbstractService
{
	@Override
	public String getMetadata()
	{
		return new MetadataBuilder()
				  .produce(Uri.TOKEN)
				  .produceFormat(Uri.LAPPS)
				  .requireFormat(Uri.LAPPS)
				  .build();
	}
}

class OpenNLPTagger extends AbstractService
{
	@Override
	public String getMetadata()
	{
		return new MetadataBuilder()
				  .produce(Uri.POS)
				  .require(Uri.TOKEN)
				  .produceFormat(Uri.LAPPS)
				  .requireFormat(Uri.LAPPS)
				  .build();
	}
}

class MetadataBuilder {
	protected ServiceMetadata metadata;

	public MetadataBuilder() {
		metadata = new ServiceMetadata();
		metadata.setVendor("http://www.anc.org");
		metadata.setVersion("2.0.0");
		metadata.setLicense(Uri.APACHE2);
		metadata.setAllow(Uri.ANY);
	}

	public MetadataBuilder require(String type) {
		metadata.getRequires().addAnnotation(type);
		return this;
	}

	public MetadataBuilder produce(String type) {
		metadata.getProduces().addAnnotation(type);
		return this;
	}

	public MetadataBuilder requireFormat(String format) {
		metadata.getRequires().addFormat(format);
		return this;
	}

	public MetadataBuilder produceFormat(String format) {
		metadata.getProduces().addFormat(format);
		return this;
	}

	public MetadataBuilder requireEncoding(String encoding) {
		metadata.getRequires().setEncoding(encoding);
		return this;
	}

	public MetadataBuilder produceEncoding(String encoding) {
		metadata.getProduces().setEncoding(encoding);
		return this;
	}

	public String build() {
		Data<ServiceMetadata> data = new Data<>(Uri.META, metadata);
		String result = data.asJson();
		metadata = null;
		return result;
	}
}