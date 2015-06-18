package org.anc.grid.pipeline;

import org.anc.lapps.pipeline.Pipeline;
import org.junit.Test;
import static org.lappsgrid.discriminator.Discriminators.Uri;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Keith Suderman
 */
public class PipeListTest2
{
   @Test
   public void testValid2()
   {
      Pipeline pipeline = new Pipeline();
      pipeline.add(new ServiceOne());
      pipeline.add(new ServiceTwo());
      assertTrue(pipeline.validate());
   }

   @Test
   public void testValid3()
   {
      Pipeline pipeline = new Pipeline();
      pipeline.add(new ServiceOne());
      pipeline.add(new ServiceTwo());
      pipeline.add(new ServiceThree());
      assertTrue(pipeline.validate());
   }

   @Test
   public void testInvalid()
   {
      Pipeline pipeline = new Pipeline();
      pipeline.add(new ServiceTwo());
      pipeline.add(new ServiceOne());
      assertFalse(pipeline.validate());
   }
}

class ServiceOne extends AbstractService
{
   @Override
   public String getMetadata()
	{
		return new MetadataBuilder()
				  .require(Uri.TOKEN)
				  .produce(Uri.SENTENCE)
				  .build();
	}
}

class ServiceTwo extends AbstractService
{
	@Override
	public String getMetadata()
	{
		return new MetadataBuilder()
				  .require(Uri.SENTENCE)
				  .produce(Uri.POS)
				  .build();
	}
}

class ServiceThree extends AbstractService
{
	@Override
	public String getMetadata()
	{
		return new MetadataBuilder()
				  .require(Uri.POS)
				  .produce(Uri.NE)
				  .build();
	}
}

