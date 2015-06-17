package org.anc.lapps.pipeline;

import org.lappsgrid.metadata.ServiceMetadata;
import org.lappsgrid.serialization.Data;
import static org.lappsgrid.discriminator.Discriminators.Uri;

/**
 * @author Keith Suderman
 */
public abstract class DataMetadata extends Data<ServiceMetadata>
{
	public DataMetadata()
	{

	}

	public DataMetadata(ServiceMetadata metadata)
	{
		super();
		this.setDiscriminator(Uri.META);
		this.setPayload(metadata);
	}
}
