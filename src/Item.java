import svp.visual.tree.*;

import java.awt.*;

import java.awt.image.ImageObserver;



public class Item  extends java.lang.Object  

{

	

	// constants for activate

	static final int ACT_DBCLICK = 1;

	static final int ACT_CLICK = 2;

	static final int ACT_HIGHLIGHT = 3;



	static final int DRAW_FULL = 1;

	static final int DRAW_HIGHLIGHT = 2;

	static final int DRAW_CLEAR = 4;

	static final int DRAW_SECONDIMAGE = 8;

	

	// varibles

	String m_title;	// text string object title

	Image m_icon;	// displayed icon

	Image m_highlightIcon; // second image for hightlighting



	ItemAction m_action; // activation object



	Rectangle m_rect;	// drawing area



/* constructors that are not used removed to reduce code sixe



   public Item( String title, Image icon )

	{

		m_title = title;

		m_icon = icon;

		m_rect = new Rectangle();

	}



	public Item( String title, Image icon, ItemAction action)

	{

		m_title = title;

		m_icon = icon;

		m_action = action;

		m_rect = new Rectangle();

	}

*/

	//////////////////////////////////////////////////

	// 	public Item( Item item )

	// copy constructor

	// note action is a reference copy any changes will effect both items

	public Item( Item item )

	{

		m_title = item.getTitle();

		m_icon = item.getIcon();

		m_action = item.getAction();

		m_rect = new Rectangle();

		m_rect.equals( item.getRect() );

		m_highlightIcon = item.getHighlightIcon();

	}
	////////////////////////////////////////////////

	// 	public Item( String title )

	// construction

	public Item( String title )

	{

		m_title = title;



		// if( m_title == null ) m_title = "";

		m_rect = new Rectangle();

	}
	//////////////////////////////////////////////////

	// 	public boolean activate( int activate_type, Event )

	// activate item into some form of action input

	// doesn't use the Event object 

	public boolean activate( int activate_type, Event e )

	{

		if( m_action != null )	{

			if( (activate_type == ACT_CLICK) || (activate_type == ACT_DBCLICK) ) {

				if (activate_type == ACT_DBCLICK) m_action.executeDBLClick();

				else m_action.execute();

				return true;

			}

		}



		return false;

	}
	//////////////////////////////////////////////////

	// 	public void draw( Graphics g, ImageObserver ob, int highlight )

	//  draw ourselves onto the screen

	//	DRAW_FULL			complete redraw

	//  DRAW_HIGHLIGHT		redraw with highlight colors

	//  DRAW_CLEAR			redraw clearing highlight

	//  DRAW_SECONDIMAGE	additional flag to display highlight image	

	public void draw( Graphics g, TreeControl ob, int highlight )

	{

		int offsetx = m_rect.x;

		int offsety = m_rect.y;



		// if not a full redraw then we need to clear out backrground

		if ( (highlight & DRAW_CLEAR) > 0 || (highlight & DRAW_HIGHLIGHT) > 0  )

		{

			ob.clearRect( g, m_rect );

		}



		// if icon draw icon

		if( m_icon != null )

		{

			Image item_icon = m_icon; 

	

			int image_width = item_icon.getWidth( ob );

			int image_height = item_icon.getHeight( ob );



			// only bother if we have vaild image sizes

			if( image_width > 0 && image_height > 0 )

			{



				if( m_highlightIcon != null && (highlight & DRAW_HIGHLIGHT) >0 )

					item_icon = m_highlightIcon;



				// centre icon verticaly

				offsety += ((m_rect.height - image_height)/2); 



				// draw icon

				g.drawImage( item_icon, offsetx, offsety, ob);



				// shift offset to compensate for image

				offsetx += image_width;

			}

		}



		// change color if highlighted

		if( (highlight & DRAW_HIGHLIGHT) >0 && m_title.length() != 0)

		{	

			g.setColor( ob.m_highColour ); // red

			

			g.fillRect( 

				offsetx, 

				m_rect.y, 

				(m_rect.width - (offsetx - m_rect.x)), 

				m_rect.height );

			

			g.setColor( ob.m_highTextColour );

		}

		else // setup colors for defualt draw

		{

			g.setColor( ob.m_textColour );

		}



		offsety = m_rect.y;

		offsetx += 2;

		

		// draw title text

		offsety += ((m_rect.height - (g.getFontMetrics()).getHeight()) /2 );

		offsety += (g.getFontMetrics()).getAscent();



		g.drawString(m_title, offsetx, offsety );

	}
	//////////////////////////////////////////////////

	//	public ItemAction getAction( )

	// get action 

	public ItemAction getAction( )

	{

		return m_action;

	}
	//////////////////////////////////////////////////

	// 	public int getHeight() 

	// get height in pixels of item

	public int getHeight() 

	{ 

		return m_rect.height; 

	}
	//////////////////////////////////////////////////

	// public Image getHighlightIcon( )

	// get second image

	public Image getHighlightIcon( )

	{

		return m_highlightIcon;	

	}
	//////////////////////////////////////////////////

	//	public Image getIcon( )

	// get item icon

	public Image getIcon( )

	{

		return m_icon;	

	}
	//////////////////////////////////////////////////

	// 	public Rectangle getRect(  )

	// get the drwing area rectangle 

	public Rectangle getRect(  )

	{

		return m_rect;

	}
	//////////////////////////////////////////////////

	// 	public String getTitle( )

	// get item title

	public String getTitle( )

	{

		return m_title;	

	}
	//////////////////////////////////////////////////

	// 	public int getWidth() 

	// get width in pixels of item

	public int getWidth() 

	{ 

		return m_rect.width; 

	}
	//////////////////////////////////////////////////

	// 	public Item hitTest( int x, int y )

	// does x,y fall within the rectangle

	public Item hitTest( int x, int y )

	{

		if( m_rect.contains( x,y ) )

			return this;

		else return null;

	}
	//////////////////////////////////////////////////

	// 	public void initalise( FontMetrics fm, ImageObserver ob )

	// computes the widht and height of item

	// intalise drawing area this should be called after 

	// construction and after any changes to title or icon

	public void initalise( FontMetrics fm, ImageObserver ob )

	{

		// find width and setup

		int width = 4;

		int height = 0;



		if( m_icon != null )

		{

			width += m_icon.getWidth( ob );

			height = m_icon.getHeight( ob );

		}

		

		width += fm.stringWidth( m_title );

		

		int fontHeight = (fm.getLeading() + fm.getMaxAscent() + fm.getMaxDescent());



		if( height < fontHeight ) 

				height = fontHeight; 



		// set rectangle

		m_rect.width = width;

		m_rect.height = height;

	}
	//////////////////////////////////////////////////

	//	public Rectangle layout( int x, int y )

	// layout item for redawing

	public Rectangle layout( int x, int y )

	{

		m_rect.setLocation(x,y);

		return m_rect;

	}
	//////////////////////////////////////////////////

	//	public void setAction( ItemAction action )

	// set action

	public void setAction( ItemAction action )

	{

		m_action = action;

	}
	//////////////////////////////////////////////////

	// 	public void setHighlightIcon( Image icon )

	//  change second image 

	public void setHighlightIcon( Image icon )

	{

		m_highlightIcon = icon;	

	}
	//////////////////////////////////////////////////

	//	public void setIcon( Image icon )

	// change icon to somthing else

	public void setIcon( Image icon )

	{

		m_icon = icon;	

	}
	//////////////////////////////////////////////////

	// 	public void setTitle( String title )

	// change title to somthing else

	public void setTitle( String title )

	{

		m_title = title;	

	}
} // end item
