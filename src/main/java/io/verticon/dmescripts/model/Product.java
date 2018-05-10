package io.verticon.dmescripts.model;

import java.util.List;
import java.util.logging.Level;

import javax.persistence.*;

import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import io.verticon.dmescripts.Factory;

@Entity
@Table(name="Products")
//@EntityListeners(ProductListener.class)
public class Product {
	private static Long idCounter = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private String category;
    private String manufacturer;
    private String imageUrl;

	@OneToMany(mappedBy = "product")
	private List<Order> orders;

	public Product() {}

	public Product(String name, String type, String category, String manufacturer, String imageUrl) {
		id = idCounter++;
		this.name = name;
		this.type = type;
		this.category = category;
		this.manufacturer = manufacturer;
		this.imageUrl = imageUrl;
	}
	

	public Long getId() {
    	return id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public List<Order> getOrders() {
    	return orders;
    }

    public void setOrders(List<Order> orders) {
    	this.orders = orders;
    }

    @Override
    public boolean equals(Object that) {
        if (that == null) { return false; }
        
        if (this == that) { return true; }

        if (!Product.class.isAssignableFrom(that.getClass())) { return false; }

        final Product thatProduct = (Product) that;
        return thatProduct.getId() == this.getId();
    }

    @Override
    public String toString() {
        return name;
    }

    //**************************************************************************************************

    	
    public static class Load {

    	private DataAccessService dataService = Factory.sDataService;

        private void loadProducts() {

        	LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
        	java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit.DefaultCssErrorHandler").setLevel(Level.SEVERE); 

        	try (final WebClient webClient = new WebClient()) {

        		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        		webClient.getOptions().setCssEnabled(false);

            	HtmlPage page = webClient.getPage("https://aeroflowinc.com/shop/catheters");
            	loadType("Catheters", page);

            	page = webClient.getPage("https://aeroflowinc.com/shop/incontinence");
            	loadType("Incontinence", page);
            }
            catch (Exception ex) {
            	System.err.printf("An exception occurred while loading product data:\n%s\n", ex);
            }
        }

    	private void loadType(String type, HtmlPage page) throws Exception {

        	System.out.printf("\n%s:\n", type);

        	String className = "easycatalogimg";
        	String selector = String.format(".%s", className);
        	DomNode theCategories = page.querySelector(selector);
        	if (theCategories == null) throw new Exception("Cannot get the product categories container"); 

        	className = "product-image";
        	selector = String.format(".%s", className);
        	DomNodeList<DomNode> theAnchors = theCategories.querySelectorAll(selector);
        	if (theAnchors == null) throw new Exception("Cannot get the list of product categories"); 
        	for (DomNode node : theAnchors) {
        		HtmlAnchor anchor = (HtmlAnchor)node;
            	String category = anchor.getAttribute("title");
            	HtmlPage categoryPage = anchor.click();
            	if (categoryPage == null) throw new Exception(String.format("Cannot get the %s page", category)); 
            	loadCategory(type,  category, categoryPage);
        	}
    	}

        private void loadCategory(String type, String category, HtmlPage page) throws Exception {

        	System.out.printf("\n\t%s:\n", category);

        	DomNodeList<DomElement> theDefinitionTerms = page.getElementsByTagName("dt");
        	for (DomElement theTerm : theDefinitionTerms) {
        		if (theTerm.getTextContent().equals("Manufacturer")) {
        			for (DomElement theElement : theTerm.getParentNode().getHtmlElementDescendants()) {
        				if (theElement instanceof HtmlAnchor) {
        					HtmlAnchor theAnchor = (HtmlAnchor)theElement;
        					if (theAnchor.getHrefAttribute().contains("?manufacturer=")) {
        						String theManufacturer = theAnchor.getTextContent();
        		            	HtmlPage theManufacturerPage = theAnchor.click();
        		            	if (theManufacturerPage == null) throw new Exception(String.format("Cannot get the %s page", theManufacturer)); 
        		            	loadManufacturer(type,  category, theManufacturer, theManufacturerPage);
        					}
        				}
        			}
        			return;
        		}
        	}
        	loadManufacturer(type,  category, "<Unknown>", page);
        }

        private void loadManufacturer(String type, String category, String manufacturer, HtmlPage page) throws Exception {

        	System.out.printf("\n\t\t%s:\n", manufacturer);

        	String className = "category-products";
        	String selector = String.format(".%s", className);
        	DomNode theProducts = page.querySelector(selector);
        	if (theProducts == null) throw new Exception(String.format("Cannot get the %s products container", category)); 
    		
        	className = "main-info";
        	selector = String.format(".%s", className);
        	DomNodeList<DomNode> theProductList = theProducts.querySelectorAll(selector);
        	if (theProductList == null) throw new Exception(String.format("Cannot get the %s products list", category)); 

        	for (DomNode theProduct : theProductList) {

            	className = "product-name";
            	selector = String.format(".%s", className);
            	DomNode theProductNameNode = theProduct.querySelector(selector);
            	HtmlAnchor theProductNameAnchor = (HtmlAnchor) theProductNameNode.getFirstChild();
            	String theProductName = theProductNameAnchor.getTextContent();

            	className = "prolabel-wrapper";
            	selector = String.format(".%s", className);
            	DomNode theProductImageNode = theProduct.querySelector(selector);
            	HtmlImage theProductImage = (HtmlImage) theProductImageNode.getFirstByXPath("a/img");
            	String theProductImageUrl = theProductImage.getAttribute("src");

            	dataService.addProduct(new Product(theProductName, type, category, manufacturer, theProductImageUrl));

            	System.out.printf("\t\t\t%s\n", theProductName);
        	}
        }

        public static void main(String[] args) {
        	System.out.println("Loading Products ...");
        	new Load().loadProducts();
        	System.out.println("\nProducts Loaded");
        }
    }
}
