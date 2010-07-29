/**
 * Copyright (c) 2009-2010, SimpleGeo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, 
 * this list of conditions and the following disclaimer. Redistributions 
 * in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or 
 * other materials provided with the distribution.
 * 
 * Neither the name of the SimpleGeo nor the names of its contributors may
 * be used to endorse or promote products derived from this software 
 * without specific prior written permission.
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS 
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE 
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.simplegeo.client.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;

import com.simplegeo.client.query.NearbyQuery;

/**
 * A place to execute queries for multiple layers.
 * 
 * @author dsmith
 */
public class LayerManager {
	
	private List<Layer> layers;
	
	/**
	 * Builds a layer manager with a list of 
	 * {@link com.simplegeo.client.model.Layer}
	 * 
	 * @param layers
	 */
	public LayerManager(List<Layer> layers) {
		this.layers = layers;
	}

	/**
	 * Adds a {@link com.simplegeo.client.model.Layer}.
	 * 
	 * @param layer the layer to add
	 */
	public void addLayer(Layer layer) {
		this.layers.add(layer);
	}
	
	/**
	 * Removes a {@link com.simplegeo.client.model.Layer}
	 * 
	 * @param layer the layer to remove
	 */
	public void removeLayer(Layer layer) {
		this.layers.remove(layer);
	}
	
	/**
	 * Returns a {@link com.simplegeo.client.model.Layer} at
	 * the provided index.
	 * 
	 * @param index the index of the desired layer
	 * @return the layer
	 */
	public Layer get(int index) {
		return this.layers.get(index);
	}
	
	/**
	 * Calls {@link com.simplegeo.client.model.Layer#update()} on all
	 * Layer objects registered with the manager.
	 */
	public void update() {
		for(Layer layer : layers)
			try {
				layer.update();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * Calls {@link com.simplegeo.client.model.Layer#retrieve()} on all Layer
	 * objects registered with the manager.
	 */
	public void retrieve() {
		for(Layer layer : layers)
			try {
				layer.retrieve();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * Calls {@link com.simplegeo.client.model.Layer#nearby(NearbyQuery)} on all
	 * Layer objects registered with the manager.
	 * 
	 * @param query the nearby query
	 * @return 
	 */
	public List<IRecord> nearby(NearbyQuery query) {
		List<IRecord> records = new ArrayList<IRecord>();
		for(Layer layer : layers)
			try {
				records.addAll((Collection<? extends IRecord>)layer.nearby(query).getFeatures());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}

			
		return records;
	}	
}
