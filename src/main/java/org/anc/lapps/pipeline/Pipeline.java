package org.anc.lapps.pipeline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.lappsgrid.core.DataFactory;
import org.slf4j.*;

import org.lappsgrid.api.Data;
import org.lappsgrid.api.WebService;
import org.lappsgrid.discriminator.*;

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

   public Data execute(Data data)
   {
      return execute(data, false);
   }

   public Data execute(Data data, boolean validateFirst)
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
         if (data.getDiscriminator() == Types.ERROR)
         {
            return data;
         }
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
      long[] produces = producer.produces();
      long[] requires = consumer.requires();
      for (long required : requires)
      {
         if (!satisfies(required, produces))
         {
            return false;
         }
      }
      return true;
   }

   
   protected boolean satisfies(long required, long[] candidates)
   {
      for (long candidate : candidates)
      {
         if (DiscriminatorRegistry.isa(candidate, required))
         {
            return true;
         }
      }
      return false;
   }
}
