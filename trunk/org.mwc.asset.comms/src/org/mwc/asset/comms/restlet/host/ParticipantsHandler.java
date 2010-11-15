package org.mwc.asset.comms.restlet.host;

import java.util.List;
import java.util.Vector;

import org.mwc.asset.comms.restlet.data.Participant;
import org.mwc.asset.comms.restlet.data.ParticipantsResource;
import org.restlet.resource.Get;

import ASSET.Participants.Category;

public class ParticipantsHandler extends ASSETResource implements
		ParticipantsResource
{
	
	@Get
	public List<Participant> retrieve()
	{
		List<Participant> theParts = new Vector<Participant>();
		theParts.add(new Participant("aba", 12, new Category(Category.Force.BLUE, Category.Environment.SURFACE,
				Category.Type.FRIGATE)));
		theParts.add(new Participant("BBB", 31, new Category(Category.Force.RED, Category.Environment.SURFACE,
				Category.Type.FRIGATE)));
		theParts.add(new Participant("CCC", 15, new Category(Category.Force.GREEN, Category.Environment.SURFACE,
				Category.Type.FRIGATE)));
		theParts.add(new Participant("ddd", 18, new Category(Category.Force.BLUE, Category.Environment.AIRBORNE,
				Category.Type.FRIGATE)));
		return theParts;
	}
	
	
}