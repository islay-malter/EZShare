/*
 * Distributed Systems
 * Group Project 1
 * Sem 1, 2017
 * Group: AALT
 * 
 * This class keeps track of which resources exist on this server.
 * It implements the basic list functions [add, remove, indexOf] using 'synchronised' for concurrency protection.
 */

package server;

import java.util.ArrayList;

public class ResourceList {
	private ArrayList<Resource> resourceList;
	public ResourceList() {
		this.resourceList = new ArrayList<Resource>();
	}
  
	public synchronized void addResource(Resource newResource) throws serverException {
		//Check if resource already exists with same channel and URI
		Resource match = queryForChannelURI(newResource);
		if(match.equals(null)) {
			if(!resourceList.add(newResource)) {
				throw new serverException("cannot publish resource");
			}
		} else {
			if(!resourceList.remove(match) || !resourceList.add(newResource)) 
				throw new serverException("cannot publish resource");
		}
		
	}
	
	public synchronized void removeResource(Resource oldResource) throws serverException {
		Resource match = queryForPK(oldResource);
		if(match.equals(null)) {
			throw new serverException("cannot remove resource");
		} else {
			if(!resourceList.remove(match)) throw new serverException("cannot remove resource");
		}
	}

	public Resource queryForPK(Resource re) {
	    //Check if resource already exists...
		int len = resourceList.size();
		if(len == 0) return null;
		
		for(int i = 0; i < len; i++) {    //...return its position in the list if it does.
			if(re.getChannel().equals(resourceList.get(i).getChannel()) && 
				re.getOwner().equals(resourceList.get(i).getOwner()) &&
				re.getURI().equals(resourceList.get(i).getURI())) {
				return resourceList.get(i);
			}
		}
		return null;
	}
	
	public Resource queryForChannelURI(Resource re) {
		int len = resourceList.size();
		if(len == 0) return null;
		
		for(int i = 0; i < len; i++) {    //...return its position in the list if it does.
			if((re.getOwner().equals(resourceList.get(i).getOwner())) &&
					(re.getURI().equals(resourceList.get(i).getURI()))) {
				return resourceList.get(i);
			}
		}
		return null;
	}
	
	public int getSize() {
		return resourceList.size();
	}
	
	//TODO Is this bad practice? [used for the QUERY command]
	public ArrayList<Resource> getResList(){
	    return this.resourceList;
	}
}