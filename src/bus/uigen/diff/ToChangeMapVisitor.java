
package bus.uigen.diff;

import java.util.LinkedHashMap;
import java.util.Map;

import de.danielbechler.diff.node.Node;
import de.danielbechler.diff.path.PropertyPath;
import de.danielbechler.diff.visitor.PrintingVisitor;
import de.danielbechler.util.Strings;


public class ToChangeMapVisitor extends PrintingVisitor
{
	
	private final Map<PropertyPath, ChangeDescription> propetyPathToChangeDescription = new LinkedHashMap();

	public ToChangeMapVisitor(final Object working, final Object base)
	{
		super(working, base);
	}
	

	@Override
	protected void print(final String text)
	{
	}

	@Override
	protected String differenceToString(final Node node, final Object base, final Object modified)
	{
		propetyPathToChangeDescription.put(node.getPropertyPath(), new AChangeDescription(node.getState(), base, modified));
//		final String text = super.differenceToString(node, base, modified);
//		messages.put(node.getPropertyPath(), text);
//		propetyPathToChangeDescription.put(key, value)
		return "";
	}
	
//    ChangeDescription translateState(final Node.State state, final Object base, final Object modified)
//	{
//    	return new AChangeDescription(state, base, modified);
//		
//	}
//	
	
	public  Map<PropertyPath, ChangeDescription> getChangeMap() {
		return propetyPathToChangeDescription;
	}
//
//	public void clear()
//	{
//		messages.clear();
//	}
//
//	public Map<PropertyPath, String> getMessages()
//	{
//		return messages;
//	}
//
//	public String getMessage(final PropertyPath path)
//	{
//		return messages.get(path);
//	}
//
//	public boolean hasMessages()
//	{
//		return !messages.isEmpty();
//	}
//
//	public String getMessagesAsString()
//	{
//		final StringBuilder sb = new StringBuilder();
//		for (final String message : messages.values())
//		{
//			sb.append(message).append('\n');
//		}
//		return sb.toString();
//	}
//
//	@Override
//	public String toString()
//	{
//		return getMessagesAsString();
//	}
}
