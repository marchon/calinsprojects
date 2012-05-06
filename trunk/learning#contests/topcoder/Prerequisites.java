import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;


public class Prerequisites {
	
	private static class Class implements Comparable<Class> {
		private String name;
		private String dep;
		private int num;
		private Set<Class> prereqisites;
		private boolean valid = false;
		
		public Class(String name) {
			this.name = name;
		}
		
		@Override
		public int hashCode() {
			return name.hashCode();
		}
		
		@Override
		public boolean equals(Object obj) {
			Class c = (Class)obj;
			if(c == null)
				return false;
			
			return c == this || name.equals(c.name);
		}
		
		@Override
		public String toString() {
			return dep + num;
		}
		
		public int compareTo(Class o) {
			int rez = prereqisites.size() - o.prereqisites.size();
			
			if(rez == 0) {
				rez = num - o.num;
			}
			
			if(rez == 0) {
				rez = dep.compareTo(o.dep);
			}
			
			return rez;
		}
	}
	
	private Class parseClass(String cls) {
		if(cls.length() < 6 || cls.length() > 7) return null;
		
		try {
			int num = Integer.parseInt(cls.substring(cls.length() - 3));
			String dep = cls.substring(0, cls.length() - 3);
			
			Class ret = new Class(cls);
			ret.dep = dep;
			ret.num = num;
			
			ret.prereqisites = new HashSet<Class>();
			
			return ret;
		} catch (Exception e) {
		}

		return null;
	}
	
	private Map<String, Class> pcls = new HashMap<String, Class>();
	
	private Class getClass(String cls, boolean valid) {
		Class clsInst = pcls.get(cls);
		if(clsInst == null) {
			clsInst = parseClass(cls);
			if(clsInst != null) {
				pcls.put(cls, clsInst);
			}
		}
		
		if(clsInst != null && valid) clsInst.valid = true;
		
		return clsInst;
	}
	
	public String[] orderClasses(String[] classes){
		//build data structure
		for (int i = 0; i < classes.length; i++) {
			String[] clst = classes[i].split(" ");
			
			String cls = clst[0].substring(0, clst[0].length() - 1);
			
			Class clsInst = getClass(cls, true);
			if(clsInst == null) return new String[0];
			
			for (int j = 1; j < clst.length; j++) {
				Class preInst = getClass(clst[j], false);
				if(preInst == null) return new String[0];
				
				clsInst.prereqisites.add(preInst);
			}
		}
		
		Collection<Class> clss = pcls.values();
		for (Class cls : clss) {
			//prerequisite but not class, error
			if(!cls.valid) {
				return new String[0];
			}
		}
		
		PriorityQueue<Class> pq = new PriorityQueue<Class>(clss);
		Class head;
		int pos = 0;
		
		Set<Class> toUpdate = new HashSet<Class>();
		while((head = pq.poll()) != null) {
			if(head.prereqisites.size() > 0) {
				return new String[0];
			}
			
			//put it in result
			classes[pos++] = head.name;
			
			//find 
			for (Iterator<Class> iterator = pq.iterator(); iterator.hasNext();) {
				Class toCheck = iterator.next();
				
				//if toCheck has this prerequisite we must update
				//so remove it from queue and add it to the update list
				// and also delete the prerequisite
				if(toCheck.prereqisites.remove(head)) {
					toUpdate.add(toCheck);
					iterator.remove();
				}
			}
			
			pq.addAll(toUpdate);
			toUpdate.clear();
		}
		
		return classes;
	}
}
