package ch.psi.synopview;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.awt.image.ImageObserver;
import ch.psi.synopview.svp.visual.tree.*;
import ch.psi.synopview.svp.devices.Device;
import ch.psi.synopview.svp.devices.*;

public class TreeControl extends ScrPane implements KeyListener, MouseListener
{
	DrawingSurface drawingSurface;
	
	FolderItem m_folder;
	Item m_selected;
	Item m_mouseHit;
	int m_activation_type;

	int m_layoutOffsetX;
	int m_layoutOffsetY;

	public Color m_detailColour;    // directory lines color  
	public Color m_highColour;  // hightlight background colour
	public Color m_textColour;	 // text colour	
	public Color m_highTextColour; // text highligh colour

	public boolean init = false;

	public TreeControl(DrawingSurface drawingSurface)
	{
		this.drawingSurface=drawingSurface;
		
		m_folder = new FolderItem("Directory");
		m_selected = m_folder;

		m_activation_type = 0;

		// setup default colors
		m_detailColour = Color.lightGray;
		m_highColour = new Color(153,0,66); // rose red
		m_textColour = Color.black;
		m_highTextColour = Color.white;

		m_layoutOffsetX = 1;
		m_layoutOffsetY = 1;
	
		setBackground( m_bgColour );

		addKeyListener(this);
		addMouseListener(this);
	}
	public void activate( )
	{
		if( m_selected != null )
			m_selected.activate( Item.ACT_CLICK, null );			
	}
	public void draw()
	{
		if (init) {
		
		Rectangle rect  = m_folder.layout( m_layoutOffsetX, m_layoutOffsetY );
		setSize( rect.x + rect.width, rect.y + rect.height );

		Graphics g = getCanvas();
	
		clearRect( g );
		m_folder.draw( g, this, Item.DRAW_FULL );

		if( m_selected != null ) 
			m_selected.draw( g, this, Item.DRAW_HIGHLIGHT );
		
		refresh();
		}
	}
	public FolderItem getFolder()
	{
		return m_folder;	
	}
/**
 * Insert the method's description here.
 * Creation date: (18/7/99 15:43:19)
 * @return svp.visual.tree.Item
 */
public Item getSelected() {
	return m_selected;
}
	// find item from string eg "folder\subdir\item"
	// and set item as selected
	// returns null if not found else the item that was selected
	public Item highlight( String itemTitle )
	{
		// break string down into elements
		StringTokenizer stoke = new StringTokenizer(itemTitle, "\\/", false);

		FolderItem parent = m_folder;
		Item currentItem = null;
		boolean redrawFlag = false; // redraw flag

		while( stoke.hasMoreElements() )
		{
			if( !parent.isExpanded() )
			{
				// expand folder
				parent.expand( true );
				redrawFlag = true;
			}
			
			// find item in the list
			if((currentItem = parent.findItem( stoke.nextToken() )) != null ) 
			{
				// if more elements to come
				if( stoke.hasMoreElements() )
				{
					if( currentItem instanceof FolderItem )
					{
						// if more elements make parent currentItem
						parent = (FolderItem)currentItem;
					}	
					else // error we are not a folder and we need one 
					{	
						// validate any changes
						if(	redrawFlag ) draw();
						
						return null; // return not found 	
					}
				} 
			}
			else 
			{
				// validate any changes
				if(	redrawFlag ) draw();
				
				// error no item by that name return not found
				return null; 
			}// end else not found
		
		} // end while tokens

		// redraw screen as we have changed the number of visible items
		if(	redrawFlag ) draw();
		// and set this item as selected
		setSelected( currentItem );
				
		return currentItem;
	}
/**
 * Insert the method's description here.
 * Creation date: (28.1.2000 17:53:59)
 * @param device svp.devices.Device
 */
public void hiliteDevice(Device device) {
	String path = search4Group(device.getParentName(), drawingSurface.getHierarchy());
	if (path==null) return;
	
	path+="\\"+device.getName();
	highlight(path);
}
	public void initalise( )
	{
		// load all the images
		Font font = getFont(); 
		if( font == null )
		{
			if( (font = new Font( "Dialog", 0 ,10)) != null )
				setFont( font );
		}

		// set the background color if changed
		m_folder.expand( true );

		// setup all the components
		m_folder.initalise( getFontMetrics( font ) , this );
	
		draw();
	}
/**
 * This method was created in VisualAge.
 * @param e java.awt.event.KeyEvent
 */
public void keyPressed(KeyEvent e) {
	int key = e.getKeyCode();
	
		Item next = null;
		// up arrow previous item
		if( key == KeyEvent.VK_UP ) {
			prev( false );		
		}

		// down arrow select next item
		else if( key == KeyEvent.VK_DOWN ) {
			next( false );
		}

		// enter of right arrow activate item
		else if( (key == KeyEvent.VK_ENTER) || (
				 (key == KeyEvent.VK_RIGHT) && (m_selected instanceof FolderItem))) 	{
			if( m_selected.activate( Item.ACT_DBCLICK, null ) )	{
				draw();	
				makeVisible( m_selected.getRect() );	
			}
		}

		// left arrow close up folder
		else if( key == KeyEvent.VK_LEFT )	{
			if ( m_selected instanceof FolderItem )	{
				FolderItem item = (FolderItem)m_selected;
				
				if( item.isExpanded() )	{
					item.expand( false );
					draw();				
				}
			}
		}

}
/**
 * This method was created in VisualAge.
 * @param e java.awt.event.KeyEvent
 */
public void keyReleased(java.awt.event.KeyEvent e) {
}
/**
 * This method was created in VisualAge.
 * @param e java.awt.event.KeyEvent
 */
public void keyTyped(java.awt.event.KeyEvent e) {
}
/**
 * Insert the method's description here.
 * Creation date: (4/9/99 16:09:55)
 * @param hierarchy java.util.Hashtable
 */
public void loadHierarchy(Hashtable hierarchy) {
	FolderItem folder = getFolder();

	folder.setTitle("Loading...");
	initalise();

	loadSubHierarchy(folder, hierarchy);

	folder.setTitle("Synchrotron");

	initalise();
}
/**
 * Insert the method's description here.
 * Creation date: (4/9/99 16:13:47)
 * @param folder svp.visual.tree.FolderItem
 * @param hierarchy java.util.Hashtable
 */
private void loadSubHierarchy(FolderItem folder, Hashtable hierarchy) {
	// 'hierarchy' is a table of Device(s) and/or GroupData(s) (==new folder)
	ItemAction action = new ItemAction() {
		public void execute() {
			drawingSurface.hiliteDevice(drawingSurface.getDevice(getSelected().getTitle()));
			drawingSurface.setSelectedDevice(getSelected().getTitle());  //gm02			
			drawingSurface.displayDeviceInfo(getSelected().getTitle());
		}
		public void executeDBLClick() {
			drawingSurface.showEngineeringScreen(drawingSurface.getDevice(getSelected().getTitle()));
		}
	};

	Object obj;
	Item item;
	FolderItem folderItem;
	GroupData group;
	Device dev;

	Enumeration e = hierarchy.elements();

	while (e.hasMoreElements()) {
		obj = e.nextElement();
		if (obj instanceof GroupData) {
			// this is a folder
			group = (GroupData)obj;
			folderItem = new FolderItem(group.getName());
			loadSubHierarchy(folderItem, group.getTable());
			folderItem.setAction(action);
			folder.addElement(folderItem);
		}
		else if (obj instanceof Device) {
			// this is a item
			dev = (Device)obj;
			item = new Item(dev.getName());
			item.setAction(action);
			folder.addElement(item);
		}
		
	}

	//initalise();
}
/**
 * mouseClicked method comment.
 */
public void mouseClicked(java.awt.event.MouseEvent e) {
}
/**
 * mouseEntered method comment.
 */
public void mouseEntered(java.awt.event.MouseEvent e) {}
/**
 * mouseExited method comment.
 */
public void mouseExited(java.awt.event.MouseEvent e) {}
	public boolean mouseMove( Event e, int x, int y )
	{
		Item item = null;
			
		if((item = m_folder.hitTest( x+m_offsetx , y+m_offsety)) != null )
		{
			item.activate( Item.ACT_HIGHLIGHT, null  );
		}

		return true;
	}
/**
 * mousePressed method comment.
 */
public void mousePressed(java.awt.event.MouseEvent e) {

   if((m_mouseHit = m_folder.hitTest( e.getX() + m_offsetx, e.getY() + m_offsety)) != null )	{
	if( m_mouseHit != null ) 
		m_mouseHit.draw( getCanvas(), this, Item.DRAW_HIGHLIGHT );

/*		// catch double clicks
		if( e.getClickCount() > 1 ) 
			m_activation_type = Item.ACT_DBCLICK;
		else 
			m_activation_type = Item.ACT_CLICK;
*/
		refresh();
	}
		
}
/**
 * mouseReleased method comment.
 */
public void mouseReleased(java.awt.event.MouseEvent e) {

	if( m_mouseHit != null ) {
		Item item = m_folder.hitTest( e.getX() + m_offsetx , e.getY() + m_offsety);

		if( item == m_mouseHit ) {
			Event ne = new Event(null, 0, null);
			ne.x += m_offsetx;
			ne.y += m_offsety;

			// setup activation 
			if( e.getClickCount() > 1 ) m_activation_type = Item.ACT_DBCLICK;
			else  m_activation_type = Item.ACT_CLICK;

			setSelected( item );
			// acitvate item
			if( item.activate( m_activation_type, ne ) ) 	{
				//m_selected = item;
				draw();				
				makeVisible( item.getRect() );	
			}
/*			else {
				setSelected( item );
			}*/
			// clear activation
			m_activation_type = Item.ACT_CLICK;
			m_mouseHit = null;
		}
		else
		{ // clear mouse hit item
			Graphics g = getCanvas();
			m_mouseHit.draw( g, this, Item.DRAW_CLEAR );
			refresh();
		}
	}

}
	public void next( boolean expand )
	{
		Item next = null;

		if( (next = m_folder.getNext( m_selected, expand )) != null )
		{
			if( expand ) draw();
			setSelected( next );
		}
	}
/**
 * Insert the method's description here.
 * Creation date: (17/7/99 17:38:30)
 */
public void paint(Graphics g) {
	if (init) super.paint(g);
	else {
		Rectangle rect  = m_folder.layout( m_layoutOffsetX, m_layoutOffsetY );
		setSize( rect.x + rect.width, rect.y + rect.height );

		clearRect( g );
		m_folder.draw( g, this, Item.DRAW_FULL );

		if( m_selected != null ) 
			m_selected.draw( g, this, Item.DRAW_HIGHLIGHT );

		init=true;
		draw();
	}
}
/**
 * Insert the method's description here.
 * Creation date: (4/9/99 17:01:35)
 * @return java.awt.Dimension
 */
public Dimension preferredSize() {
	// x = the initial size of the tree panel...
	return new Dimension(200, 300);
}
	public void prev( boolean expand )
	{
		Item next = null;

		if( (next = m_folder.getPrev( m_selected, expand )) != null )
		{
			if( expand ) draw();
			setSelected( next );
		}
	}
/**
 * Insert the method's description here.
 * Creation date: (4/9/99 16:54:54)
 * @return java.util.Hashtable
 * @param folder FolderItem
 * @param hierarchy java.util.Hashtable
 */
private Hashtable search4FolderItem(FolderItem folder, Hashtable hierarchy) {
	Object obj;
	GroupData group;
	Hashtable table;
	Device dev;

	Enumeration e = hierarchy.elements();

	while (e.hasMoreElements()) {
		obj = e.nextElement();
		if (obj instanceof GroupData) {
			group = (GroupData)obj;
			if (folder.getTitle().equals(group.getName())) return group.getTable();
			else {
				table = search4FolderItem(folder, group.getTable());
				if (table!=null) return table;
			}			
		}
		
	}

	return null;
}
/**
 * Insert the method's description here.
 * Creation date: (28/01/00 17:54:54)
 * @return java.lang.String
 * @param groupName String
 * @param hierarchy java.util.Hashtable
 */
private String search4Group(String groupName, Hashtable hierarchy) {
	Object obj;
	GroupData group;
	Device dev;
	String grp;

	Enumeration e = hierarchy.elements();

	while (e.hasMoreElements()) {
		obj = e.nextElement();
		if (obj instanceof GroupData) {
			group = (GroupData)obj;
			if (groupName.equals(group.getName())) return group.getName();
			else {
				grp = search4Group(groupName, group.getTable());
				if (grp!=null) return group.getName()+"\\"+grp;
			}			
		}
		
	}

	return null;
}
	public FolderItem setFolder( FolderItem folder )
	{
		FolderItem base = m_folder;
		m_folder = folder;
		
		m_selected = null;
		m_mouseHit = null;
		m_activation_type = 0;

		m_selected = m_folder;
		initalise();	
		
		return base;
	}
	// set layout offset for the directory structure
	public void setOffset( int x, int y )
	{
		m_layoutOffsetX = x;
		m_layoutOffsetY = y;
	}
	// set an item as selected
	public void setSelected( Item item )
	{
		if( m_selected != item )
		{
			Graphics g = getCanvas();

			item.draw( g, this, Item.DRAW_HIGHLIGHT );
			
			if( m_selected != null )
			{
				m_selected.draw( getCanvas(), this, Item.DRAW_CLEAR );
			}

			m_selected = item;

			if(!makeVisible( item.getRect() ) )	
				refresh();

			// activate item
			m_selected.activate( Item.ACT_HIGHLIGHT, null  );
		}
	}
/**
 * Sets visibility of currently selected folder
 * Creation date: (4/9/99 16:39:04)
 * @param state boolean
 */
public void setVisibility(boolean state) {
	Item item = getSelected();

	if (item instanceof FolderItem) {
		FolderItem folderItem = (FolderItem)item;
		Hashtable folder;
		if (item == getFolder()) folder = drawingSurface.getHierarchy();
		else folder = search4FolderItem(folderItem, drawingSurface.getHierarchy());
		
		if (folder!=null) {
			drawingSurface.setVisibility(folder, state);
			drawingSurface.repaint();			
		}
	}
}
}
