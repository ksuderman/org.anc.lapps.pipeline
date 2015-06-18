package org.anc.lapps.pipeline;

import groovy.lang.MetaClass;
import org.lappsgrid.metadata.ServiceMetadata;
import org.lappsgrid.serialization.Data;
import static org.lappsgrid.discriminator.Discriminators.Uri;

/**
 * @author Keith Suderman
 */
public class DataMetadata extends Data<ServiceMetadata>
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

	@Override
	public Object invokeMethod(String s, Object o)
	{
		return null;
	}

	@Override
	public Object getProperty(String s)
	{
		return null;
	}

	@Override
	public void setProperty(String s, Object o)
	{

	}

	@Override
	public MetaClass getMetaClass()
	{
		return null;
	}

	@Override
	public void setMetaClass(MetaClass metaClass)
	{

	}
}
