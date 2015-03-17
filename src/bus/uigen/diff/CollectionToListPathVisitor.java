
package bus.uigen.diff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//import de.danielbechler.diff.node.CollectionNode;
import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.path.NodePath;
import de.danielbechler.diff.node.PrintingVisitor;


public class CollectionToListPathVisitor extends PrintingVisitor
{
	private final Map<List, ChangeDescription> propetyPathToChangeDescription = new LinkedHashMap();

//	private final Map<PropertyPath, ChangeDescription> propetyPathToChangeDescription = new LinkedHashMap();

	public CollectionToListPathVisitor(final Object working, final Object base)
	{
		super(working, base);
	}
	

	@Override
	protected void print(final String text)
	{
	}

	@Override
	protected String differenceToString(final DiffNode node, final Object base, final Object modified)
	{
//		NodePath propertyPath = node.getPropertyPath();
		NodePath propertyPath = node.getPath();

		List listPath = new ArrayList();
//		for (Object element: propertyPath.getElements()) {
		for (Object element: propertyPath.getElementSelectors()) {

			listPath.add(element);
		}
		DiffNode parentNode = node.getParentNode();
//		if (! (parentNode instanceof CollectionNode)) return "";
		if (! (parentNode.hasChildren())) return "";

		
		
//			CollectionNode collectionParentNode = (CollectionNode) parentNode;
			Collection collectionBase = (Collection) parentNode.canonicalGet(base);
			Collection collectionModified =  (Collection) parentNode.canonicalGet(modified);
			if  (! (collectionBase instanceof List) && ! (collectionModified instanceof List)) return "";
			List listBase = (List) collectionBase;
			List listModified = (List) collectionModified;
			int index;
			List elements= propertyPath.getElementSelectors();
			switch (node.getState()) {
			case ADDED: // in modified
				 index = listModified.indexOf( node.canonicalGet(modified));
				 listPath.set(listPath.size() -1, index);
//				elements.set(elements.size() - 1, index);
				break;
			case REMOVED: // in base
				index = listModified.indexOf(node.canonicalGet(base));
				listPath.set(listPath.size() - 1, index);
				break;
			case CHANGED: // in base and modified
				index = listModified.indexOf(node.canonicalGet(base));
				listPath.set(listPath.size() - 1, index);
				break;	
				
			
		}
			propetyPathToChangeDescription.put(listPath, new AChangeDescription(node.getState(), base, modified));

//			propetyPathToChangeDescription.put(listPath, new AChangeDescription(node.getState(), base, modified));
		
//		propetyPathToChangeDescription.put(node.getPropertyPath(), new AChangeDescription(node.getState(), base, modified));
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
	
//	public  Map<PropertyPath, ChangeDescription> getChangeMap() {
//		return propetyPathToChangeDescription;
//	}
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
