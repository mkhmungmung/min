package min.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 개요 : XML 형식의 데이터 파일 처리를 위한 기능을 제공합니다.<br>
 * 작성자 : kh-min<br>
 * Version : 1.00
 */
public class MinXml 
{
	public static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	public static DocumentBuilder builder;
	private Document doc;
	private Node rootNode;
	
	/**
	 * 생성자
	 * @param stream
	 */
	public MinXml(InputStream stream) 
	{
		try 
		{
			if(builder == null)
			{
				factory.setValidating(false);
				factory.setIgnoringComments(false);
				factory.setIgnoringElementContentWhitespace(true);
				factory.setNamespaceAware(true);
				builder = factory.newDocumentBuilder();
			}
			doc = builder.parse(stream);
			rootNode = doc.getFirstChild();
			stream.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 생성자
	 * @param xmlPath
	 */
	public MinXml(URL xmlPath) 
	{
		try 
		{
			if(builder == null)
			{
				factory.setValidating(false);
				factory.setIgnoringComments(false);
				factory.setIgnoringElementContentWhitespace(true);
				factory.setNamespaceAware(true);
				builder = factory.newDocumentBuilder();
			}
			doc = builder.parse(xmlPath.openStream());
			rootNode = doc.getFirstChild();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 생성자
	 * @param doc
	 */
	public MinXml(Document doc)
	{
		this.doc = doc;
		this.rootNode = doc.getFirstChild();
	}
	
	/**
	 * 생성자
	 * @param rootNode
	 */
	public MinXml(Node rootNode)
	{
		this.rootNode = rootNode;
		this.doc = rootNode.getOwnerDocument();
	}
	
	/**
	 * 생성자
	 * 새로 xml 데이터를 생성합니다.
	 * @param root
	 */
	public MinXml(String root)
	{
		try
		{
			if(builder == null)
			{
				factory.setValidating(false);
				factory.setIgnoringComments(false);
				factory.setIgnoringElementContentWhitespace(true);
				factory.setNamespaceAware(true);
				builder = factory.newDocumentBuilder();
			}
			doc = builder.newDocument();
			rootNode = doc.createElement(root);
			doc.appendChild(rootNode);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 생성자
	 * 새로 xml 데이터를 생성합니다.
	 * @param root
	 * @param attributes
	 */
	public MinXml(String root, ArrayList<MinProperties> attributes)
	{
		try
		{
			if(builder == null)
			{
				factory.setValidating(false);
				factory.setIgnoringComments(false);
				factory.setIgnoringElementContentWhitespace(true);
				factory.setNamespaceAware(true);
				builder = factory.newDocumentBuilder();
			}
			doc = builder.newDocument();
			rootNode = doc.createElement(root);
			doc.appendChild(rootNode);
			for(MinProperties attribute : attributes)
			{
				if(rootNode instanceof Element)
					((Element)rootNode).setAttribute(attribute.getKey(), attribute.getStringValue());
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Root Node 의 ChildNode 를 생성합니다.
	 * @param tagName
	 * @return
	 */
	public MinXml createChildNode(String tagName)
	{
		Element node = doc.createElement(tagName);
		rootNode.appendChild(node);
		return new MinXml(node);
	}
	
	/**
	 * Root Node 의 ChildNode 를 생성합니다.
	 * @param tagName
	 * @param content
	 * @return
	 */
	public MinXml createChildNode(String tagName, String content)
	{
		Element node = doc.createElement(tagName);
		if(content != null)
		{
			node.setTextContent(content);
		}
		rootNode.appendChild(node);
		return new MinXml(node);
	}
	
	/**
	 * Root Node 의 ChildNode 를 생성합니다.
	 * @param tagName
	 * @param attributes
	 * @return
	 */
	public MinXml createChildNode(String tagName, ArrayList<MinProperties> attributes)
	{
		Element node = doc.createElement(tagName);
		for(MinProperties attribute : attributes)
		{
			node.setAttribute(attribute.getKey(), attribute.getStringValue());
		}
		rootNode.appendChild(node);
		return new MinXml(node);
	}
	
	/**
	 * Root Node 의 ChildNode 를 생성합니다.
	 * @param tagName
	 * @param content
	 * @param attributes
	 * @return
	 */
	public MinXml createChildNode(String tagName, String content, ArrayList<MinProperties> attributes)
	{
		Element node = doc.createElement(tagName);
		if(content != null)
		{
			node.setTextContent(content);
		}
		for(MinProperties attribute : attributes)
		{
			node.setAttribute(attribute.getKey(), attribute.getStringValue());
		}
		rootNode.appendChild(node);
		return new MinXml(node);
	}
	
	/**
	 * Root Node 의 ChildNode 를 생성합니다.
	 * @param node
	 */
	public void createChildNode(Node node)
	{
		rootNode.appendChild(node);
	}
	
	/**
	 * Root Node 의 ChildNode 를 생성합니다.
	 * @param node
	 */
	public void createChildNode(MinXml node)
	{
		rootNode.appendChild(node.getRootNode());
	}
	
	/**
	 * Root Node 의 ChildNode 리스트를 반환합니다.
	 * @return
	 */
	public ArrayList<MinXml> getChildNodes()
	{
		ArrayList<MinXml> childNodes = new ArrayList<MinXml>();
		for(int i=0; i<rootNode.getChildNodes().getLength(); i++)
		{
			Node childNode = rootNode.getChildNodes().item(i);
			if(childNode != null && childNode.getLocalName() != null)
			{
				childNodes.add(new MinXml(childNode));
			}
		}
		return childNodes;
	}
	
	/**
	 * LocalName 에 해당하는 Node 를 검색하여 반환합니다.
	 * @param localName
	 * @return
	 */
	public MinXml searchChildNode(String localName)
	{
		return searchChildNode(rootNode, localName);
	}
	
	/**
	 * LocalName 에 해당하는 Node 를 검색하여 리스트로 반환합니다.
	 * @param localName
	 * @return
	 */
	public ArrayList<MinXml> searchChildNodes(String localName)
	{
		return searchChildNodes(rootNode, localName);
	}
	
	/**
	 * Node 의 ChildNode 중 LocalName 에 해당하는 Node 를 검색하여 리스트로 반환합니다.
	 * @param parentNode
	 * @param localName
	 * @return
	 */
	private ArrayList<MinXml> searchChildNodes(Node parentNode, String localName)
	{
		ArrayList<MinXml> returnNodeList = new ArrayList<MinXml>();
		if(parentNode != null)
		{
			NodeList childNodes = parentNode.getChildNodes();
			for(int i=0; i<childNodes.getLength(); i++)
			{
				// 검색 조건이 null 이면 null 이 설정된 Node 를 추가합니다.
				if(localName == null && childNodes.item(1).getLocalName() == null)
				{
					returnNodeList.add(new MinXml(childNodes.item(i)));
				}
				else
				{
					//  Local Name 이 같은 Node 를 추가합니다.
					if(localName != null && localName.equals(childNodes.item(i).getLocalName()))
					{
						returnNodeList.add(new MinXml(childNodes.item(i)));
					}
				}
			}
		}
		return returnNodeList;
	}
	
	/**
	 * Node 의 ChildNode 중 LocalName 에 해당하는 Node 를 검색하여 리스트로 반환합니다.
	 * @param parentNode
	 * @param localName
	 * @return
	 */
	private MinXml searchChildNode(Node parentNode, String localName)
	{
		NodeList childNodes = parentNode.getChildNodes();
		for(int i=0; i<childNodes.getLength(); i++)
		{
			// 검색 조건이 null 이면 node 에 null 이 설정된 것을 찾아서 추가합니다.
			if(localName == null && childNodes.item(1).getLocalName() == null)
			{
				return new MinXml(childNodes.item(i));
			}
			else
			{
				//  Local Name 이 같은 Node 를 추가합니다.
				if(localName != null && localName.equals(childNodes.item(i).getLocalName()))
				{
					return new MinXml(childNodes.item(i));
				}
			}
		}
		return null;
	}
	
	/**
	 * attribute id 의 값이 value 와 같은 Node 를 반환합니다. 
	 * @param attributeId
	 * @param value
	 * @return
	 */
	public MinXml searchChildNode(String attributeId, String value)
	{
		return searchChildNode(rootNode.getChildNodes(), attributeId, value);
	}
	
	/**
	 * attribute id 값이 value 와 같은 Node 의 리스트를 반환합니다.
	 * @param attributeId
	 * @param value
	 * @return
	 */
	public ArrayList<MinXml> searchChildNodes(String attributeId, String value)
	{
		return searchChildNodes(rootNode.getChildNodes(), attributeId, value);
	}
	
	/**
	 * attribute id 의 값이 value 와 같은 Node 를 반환합니다. 
	 * @param localName
	 * @param attributeId
	 * @param value
	 * @return
	 */
	public MinXml searchChildNode(String localName, String attributeId, String value)
	{
		ArrayList<MinXml> searchNodes = searchChildNodes(localName);
		return searchChildNode(searchNodes, attributeId, value);
	}
	
	/**
	 * attribute id 값이 value 와 같은 Node 의 리스트를 반환합니다.
	 * @param localName
	 * @param attributeId
	 * @param value
	 * @return
	 */
	public ArrayList<MinXml> searchChildNodes(String localName, String attributeId, String value)
	{
		ArrayList<MinXml> searchNodes = searchChildNodes(localName);
		return searchChildNodes(searchNodes, attributeId, value);
	}
	
	/**
	 * attribute id 의 값이 value 와 같은 Node 를 반환합니다. 
	 * @param childNodes
	 * @param attributeId
	 * @param value
	 * @return
	 */
	public MinXml searchChildNode(NodeList childNodes, String attributeId, String value)
	{
		for(int i=0; i<childNodes.getLength(); i++)
		{
			Node node = childNodes.item(i);
			String textContent = node.getAttributes().getNamedItem(attributeId).getTextContent();
			if(value != null && value.equals(textContent))
			{
				return new MinXml(node);
			}
		}
		return null;
	}
	
	/**
	 * attribute id 값이 value 와 같은 Node 의 리스트를 반환합니다.
	 * @param nodeList
	 * @param attributeId
	 * @param value
	 * @return
	 */
	public ArrayList<MinXml> searchChildNodes(NodeList childNodes, String attributeId, String value)
	{
		ArrayList<MinXml> searchNodeList = new ArrayList<MinXml>();
		for(int i=0; i<childNodes.getLength(); i++)
		{
			Node node = childNodes.item(i);
			String textContent = node.getAttributes().getNamedItem(attributeId).getTextContent();
			if(value != null && value.equals(textContent))
			{
				searchNodeList.add(new MinXml(node));
			}
		}
		return searchNodeList;
	}
	
	/**
	 * attribute id 의 값이 value 와 같은 Node 를 반환합니다. 
	 * @param childNodes
	 * @param attributeId
	 * @param value
	 * @return
	 */
	public MinXml searchChildNode(ArrayList<MinXml> childNodes, String attributeId, String value)
	{
		for(int i=0; i<childNodes.size(); i++)
		{
			MinXml node = childNodes.get(i);
			String textContent = node.getAttributeValue(attributeId);
			if(value != null && value.equals(textContent))
			{
				return node;
			}
		}
		return null;
	}
	
	/**
	 * attribute id 값이 value 와 같은 Node 의 리스트를 반환합니다.
	 * @param nodeList
	 * @param attributeId
	 * @param value
	 * @return
	 */
	public ArrayList<MinXml> searchChildNodes(ArrayList<MinXml> childNodes, String attributeId, String value)
	{
		ArrayList<MinXml> searchNodeList = new ArrayList<MinXml>();
		for(int i=0; i<childNodes.size(); i++)
		{
			MinXml node = childNodes.get(i);
			String attributeValue = node.getAttributeValue(attributeId);
			if(value != null && value.equals(attributeValue))
			{
				searchNodeList.add(node);
			}
		}
		return searchNodeList;
	}
	
	/**
	 * attribute id 에 해당하는 value 값을 반환합니다.
	 * @param node
	 * @param attributeId
	 * @return
	 */
	public String getAttributeValue(Node node, String attributeId)
	{
		if(node.getAttributes().getNamedItem(attributeId) != null)
		{
			return node.getAttributes().getNamedItem(attributeId).getTextContent();
		}
		return null;
	}
	
	/**
	 * attribute id 에 해당하는 value 값을 반환합니다.
	 * @param attributeId
	 * @return
	 */
	public String getAttributeValue(String attributeId)
	{
		return getAttributeValue(rootNode, attributeId);
	}
	
	/**
	 * Text Content 값을 반환합니다.
	 * @return
	 */
	public String getTextContent()
	{
		return rootNode.getTextContent();
	}
	
	/**
	 * Local Name 값을 반환합니다.
	 * @return
	 */
	public String getLocalName()
	{
		return rootNode.getLocalName();
	}
	
	/**
	 * xml 내용을 stream 에 입력합니다.
	 * @param out
	 */
	public void write(OutputStream out)
	{
		try
		{
			out.write(toString().getBytes("UTF-8"));
			out.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * xml 파일을 생성합니다.
	 * @param filePath
	 */
	public void write(String filePath)
	{
		try
		{
			File file = new File(filePath);
			file.setWritable(true);
			file.createNewFile();
			FileOutputStream out = new FileOutputStream(file);
			out.write(toString().getBytes("UTF-8"));
			out.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * xml 파일을 생성합니다.
	 * @param filePath
	 */
	public void write(URL url)
	{
		try
		{
			File file = new File(url.toURI());
			file.setWritable(true);
			file.createNewFile();
			FileOutputStream out = new FileOutputStream(file);
			out.write(toString().getBytes("UTF-8"));
			out.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * @return  the doc
	 */
	public Document getDoc() 
	{
		return doc;
	}

	/**
	 * @return  the rootNode
	 */
	public Node getRootNode()
	{
		return rootNode;
	}

	@Override
	public String toString()
	{
		StringWriter stringWriter = new StringWriter();
		try 
		{
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(new DOMSource(rootNode), new StreamResult(stringWriter));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return stringWriter.toString();
	}
	
	/**
	 * 사용 가능한 Stream 인지 여부를 반환합니다.
	 * @param stream
	 * @return
	 */
	public boolean isAvailableStream(InputStream stream)
	{
		try 
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setIgnoringComments(false);
			factory.setIgnoringElementContentWhitespace(true);
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();

			builder.parse(stream);
			return true;
		}
		catch (Exception e) 
		{
			return false;
		}
	}
}
