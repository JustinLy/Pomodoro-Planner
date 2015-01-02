import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.db4o.*;
public class testdb {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded
				 .newConfiguration(), "tunak4o");
		List<List<String>> blah = new ArrayList<List<String>>();
		List<String> today = new ArrayList<String>();
		today.add("Kill Gupta");
		List<String> tomorrow = new ArrayList<String>();
		tomorrow.add("Kill Tor");
		blah.add(today);
		blah.add(tomorrow);
		Queue<String> q = new LinkedList();
		q.add("fuck u");
				try {
				 // do something with db4o
					ObjectSet result=db.queryByExample(new Object());
					while(result.hasNext()) {
					 db.delete(result.next());
					}

					db.store(blah);
					db.store(q);
					List<List<String>> bs;
					List< List<String> > proto  = null;
				 result = db.queryByExample(proto);
				 ObjectSet result2 = db.queryByExample( Queue.class);
				 System.out.println( result2.size());
				 Queue<String> q2 = (Queue<String>)result2.next();
				 System.out.println( q2.poll());
					System.out.println( result.size() );
					List<List<String>> myResult = (List<List<String>>) result;
					System.out.println( myResult == blah);
				} finally {
				 db.close();
				}

	}

}
