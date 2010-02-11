package com.simplegeo.client.model;

public enum RecordType {
	
	OBJECT {
		
		public String toString() {
			return "object";
		}
		
	},
	
	PERSON {

		public String toString() {
			return "person";
		}
		
	},
	
	PLACE { 
		
		public String toString() {
			return "place";
		}
		
	}, 
	
	NOTE {
		
		public String toString() {
			return "note";
		}
		
	},
	
	AUDIO {
		
		public String toString() {
			return "audio";
		}
		
	},
	
	VIDEO {
		
		public String toString() {
			return "video";
		}
		
	},
	
	IMAGE {
		
		public String toString() {
			return "image";
		}

	}
	
}
