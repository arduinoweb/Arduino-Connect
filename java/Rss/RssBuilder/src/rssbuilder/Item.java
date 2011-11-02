/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rssbuilder;

/**
 *
 * @author gary
 */
public class Item {
    
    private  String title;
    private  String description;
    
    
    public Item()
    {
        title = "";
        description = "";
        
    }
    public Item( final String title, final String description)
    {
      this.title = title;
      this.description = description;
     
      
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public void setTitle( final String title)
    {
        this.title = title;
    }
    public String getDescription()
    {
        return description;    
    }
    
    public void setDescription( final String description )
    {
        this.description = description;
    }
    
   
    
    @Override
    public String toString()
    {
       return title;   
    }
    
}
