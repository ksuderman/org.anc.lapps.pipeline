package org.anc.lapps.pipeline;

import org.lappsgrid.api.WebService;
import org.lappsgrid.core.DataFactory;
import org.lappsgrid.metadata.IOSpecification;
import org.lappsgrid.metadata.ServiceMetadata;
import org.lappsgrid.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Pipeline
{
   protected static final Logger logger = LoggerFactory.getLogger(Pipeline.class);

   protected List<WebService> pipeline;

   public Pipeline()
   {
      pipeline = new ArrayList<WebService>();
   }

   public Pipeline(List<WebService> services)
   {
      pipeline = new ArrayList<WebService>(services);
   }

   public Pipeline(WebService[] services)
   {
      pipeline = new ArrayList<WebService>(Arrays.asList(services));
   }

   public void add(WebService service)
   {
      pipeline.add(service);
   }

   public String execute(String data)
   {
      return execute(data, false);
   }

   public String execute(String data, boolean validateFirst)
   {
      logger.info("Running pipeline.");
      if (validateFirst)
      {
         logger.debug("Validating the pipeline");
         if (!validate())
         {
            logger.warn("Pipeline failed validation.");
            return DataFactory.error("Pipeline failed validation.");
         }
      }
      for (WebService service : pipeline)
      {
         data = service.execute(data);
      }
      return data;
   }

   public boolean validate()
   {
      Iterator<WebService> iterator = pipeline.iterator();
      if (!iterator.hasNext())
      {
         return false;
      }

      WebService producer = iterator.next();
      while (iterator.hasNext())
      {
         WebService consumer = iterator.next();
         if (!satisfies(producer, consumer))
         {
            logger.error("{} does not satisfy {}", producer.getClass().getName(),
                    consumer.getClass().getName());
            return false;
         }
         producer = consumer;
      }
      return true;
   }

   /**
    * Returns true if the producer generates all the annotations, in the
    * correct format, required by the consumer. Returns false otherwise.
    * @param producer
    * @param consumer
    * @return
    */
   protected boolean satisfies(WebService producer, WebService consumer)
   {
      ServiceMetadata producerMetadata = getMetadata(producer);
		ServiceMetadata consumerMetadata = getMetadata(consumer);
		IOSpecification produced = producerMetadata.getProduces();
		IOSpecification required = consumerMetadata.getRequires();

		return produced.satisfies(required);
   }

	protected ServiceMetadata getMetadata(WebService service)
	{
		DataMetadata data = Serializer.parse(service.getMetadata(), DataMetadata.class);
		return data.getPayload();
	}
   
}
