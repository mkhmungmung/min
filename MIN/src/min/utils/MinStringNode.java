package min.utils;
import java.util.ArrayList;

public class MinStringNode 
{
	private String string;
	private MinStringNode parents;
	private ArrayList<MinStringNode> nodes;
	
	/**
	 * String node 를 생성
	 * @param parents
	 * @param string
	 */
	public MinStringNode(MinStringNode parents, String string) 
	{
		this.parents = parents;
		this.string = string;
	}
	/**
	 * 
	 * @return resultList
	 */
	public ArrayList<String> getReulstList()
	{
		ArrayList<String> resultList = new ArrayList<String>();
		if(getNodes() != null && getNodes().size() > 0)
		{
			for(MinStringNode node : getNodes())
			{
				resultList.addAll(node.getReulstList());
			}
		}
		else
		{
			String string = toString();
			resultList.add(string);
		}
		return resultList;
	}
	/**
	 * @return the string
	 */
	public String getString() {
		return string;
	}
	/**
	 * @param string the string to set
	 */
	public void setString(String string) {
		this.string = string;
	}
	/**
	 * @return the parents
	 */
	public MinStringNode getParents() {
		return parents;
	}
	/**
	 * @param parents the parents to set
	 */
	public void setParents(MinStringNode parents) {
		this.parents = parents;
	}
	/**
	 * @return the nodes
	 */
	public ArrayList<MinStringNode> getNodes() {
		return nodes;
	}
	/**
	 * @param nodes the nodes to set
	 */
	public void setNodes(ArrayList<MinStringNode> nodes) {
		this.nodes = nodes;
	}
	/**
	 * 
	 * @param node
	 */
	public void addNode(MinStringNode node)
	{
		if(nodes == null)
		{
			nodes = new ArrayList<MinStringNode>();
		}
		nodes.add(node);
	}
	/**
	 * nodes clear
	 */
	public void clear()
	{
		if(nodes == null)
		{
			nodes.clear();
		}
	}
	
	@Override
	public String toString() 
	{
		String toString = string;
		if(parents != null)
		{
			toString = parents.toString() + "," + toString;
		}
		return toString;
	}
}
