package svp.visual.tree;

import java.awt.*;

public class ScrPane extends Panel
{
	Scrollbar m_hbar, m_vbar;

	public int m_offsetx, m_offsety;
	int m_image_width, m_image_height;
	Image m_image;
	Graphics m_image_graphics;

	public Color m_bgColour;    // window background colour

	Image m_backgroundImage;
	
	boolean has_vbar;	// keep track if we have scroll bars or not
	boolean has_hbar;

	public ScrPane( )
	{
		// implict super call creates panel
		super();

		m_image_width =  0;
		m_image_height = 0;

		// create our components
		m_vbar = new Scrollbar( Scrollbar.VERTICAL );
		m_hbar = new Scrollbar( Scrollbar.HORIZONTAL );

		// use a border layout manager
		this.setLayout(  new BorderLayout(0,0) );

		this.add( "East" , m_vbar ); 
		this.add( "South", m_hbar );  

		// background color
		m_bgColour = Color.white;
	}
	// clear complete drawing area
	public void clearRect(Graphics g )
	{
		g.setColor( m_bgColour );
		g.fillRect( 0,0, m_image_width, m_image_height);

		if( m_backgroundImage != null )
		{
			// draw background image
			g.drawImage( m_backgroundImage, 0,0, this );
		}
	}
	// clip a rectangle of the graphics and
	// clear that - further drawing will be
	// restricted to the clipped area as
	// there seems to be no way of turning it off
	public void clearRect(Graphics g, Rectangle rect )
	{
		g.setColor( m_bgColour );
		g.fillRect( rect.x, rect.y, rect.width, rect.height );

		if( m_backgroundImage != null )
		{
			// set our own clipping area
			g.clipRect(rect.x, rect.y, rect.width, rect.height );

			// draw background image
			g.drawImage( m_backgroundImage, 0,0, this );
		}
	}
/**
 * Insert the method's description here.
 * Creation date: (10/02/99 18:42:50)
 */
public void draw() {}
	// get a drawing Graphics for the scroll pane
	public Graphics getCanvas()
	{
		return m_image_graphics;
	}
	public Rectangle getRect( )
	{
		// get rectangle size with canvas size
		Rectangle rect = new Rectangle (getSize());
 
		// set top corner
		rect.x = m_offsetx;
		rect.y = m_offsety;
		
		// adjust width and height for scroll bars
		if( m_hbar.isVisible() )
			rect.height -=  (m_hbar.getSize()).height;

		// vertical bar
		if( m_vbar.isVisible() )
			rect.width -=  (m_vbar.getSize()).width;

		return rect;
	}
	public boolean handleEvent( Event e )
	{
		if( e.target == m_hbar )
		{
			switch( e.id )
			{
			case Event.SCROLL_LINE_UP:	
			case Event.SCROLL_LINE_DOWN:	
			case Event.SCROLL_PAGE_UP:	
			case Event.SCROLL_PAGE_DOWN:	
			case Event.SCROLL_ABSOLUTE:	
				m_offsetx = ((Integer)e.arg).intValue();
				break;
			}
			this.update( getGraphics() );
			return true;
		}

		if( e.target == m_vbar )
		{
			switch( e.id )
			{
			case Event.SCROLL_LINE_UP:	
			case Event.SCROLL_LINE_DOWN:	
			case Event.SCROLL_PAGE_UP:	
			case Event.SCROLL_PAGE_DOWN:	
			case Event.SCROLL_ABSOLUTE:	
				m_offsety = ((Integer)e.arg).intValue();
				break;
			}
			this.update( getGraphics() );
			return true;
		}

		return super.handleEvent( e );
	}
	// shift scroll postions to make the rectangle visible
	// returns true if repainted canvas
	public boolean makeVisible( Rectangle item_rect )
	{
		Rectangle our_rect = getRect();

		int our_rigthx = (our_rect.x + our_rect.width);
		int item_rigthx = (item_rect.x + item_rect.width);

		int scrollx = 0;

/*		if( our_rect.x < item_rect.x && 
			our_rigthx < item_rigthx )
		{
			// rigth over hang

			// avoid shifting our left edge off the side
			if( our_rect.width < item_rect.width )
				item_rigthx -= item_rect.width - our_rect.width; 
			
			scrollx = item_rigthx - our_rigthx;
		}
		else
		if( our_rect.x > item_rect.x)
		{	
			// left overhang
			scrollx = item_rect.x - our_rect.x;	
		}
*/
		int our_boty = (our_rect.y + our_rect.height);
		int item_boty = (item_rect.y + item_rect.height);

		int scrolly = 0;

		if( our_rect.y < item_rect.y && 
			our_boty < item_boty )
		{
			// bottom over hang

			// avoid shifting our left edge off the side
			if( our_rect.height < item_rect.height )
				item_boty -= item_rect.height - our_rect.height; 
			
			scrolly = item_boty - our_boty;
		}
		else
		if( our_rect.y > item_rect.y )
		{	
			// top overhang
			scrolly = item_rect.y - our_rect.y;	
		}

		// if any changes shift scroll position
		if( scrollx != 0 || scrolly != 0 )
		{
			 m_offsetx += scrollx;
			 m_offsety += scrolly;
			
			setSize( m_image_width, m_image_height );
			refresh();	
			return true;
		}
		return false;
		
	}
	public void paint( Graphics g )
	{
		if( m_image != null )
			g.drawImage( m_image,  -m_offsetx, -m_offsety, m_bgColour, this );
	}
	// screen refresh
	public void refresh( )
	{
		update( getGraphics() );
	}
	public synchronized void reshape( int x, int y, int width, int height )
	{
		super.reshape( x,y,width,height);

		setSize( m_image_width, m_image_height );
		draw();
		refresh();
	}
	public void setBackgroundImage( Image backImage )
	{
		m_backgroundImage = backImage;
	}
	public void setSize( int width, int height )
	{
		if( width == 0 || height == 0 ) return; // catch intailisation problems

		if( m_backgroundImage != null )
		{
			// make sure we are not smaller than the backgroung image
			if( width < m_backgroundImage.getWidth( this ) )
					width = m_backgroundImage.getWidth(this );

			// adjust height for background image maximum the canvas height
			if( height <  m_backgroundImage.getHeight( this ) )
				height =  m_backgroundImage.getHeight( this );
		}

		// if width and height have changed then reallocate an image
		if( width != m_image_width || height != m_image_height ) 
		{
			m_image_width  = width;
			m_image_height = height;

			if( m_image != null ) m_image.flush(); // clear any proccessing thats still to be done

			//  create a new image
			m_image = createImage( m_image_width, m_image_height);
		 	m_image_graphics = m_image.getGraphics();		
			m_image_graphics.setFont( getFont() );
		
		}

		boolean layoutControl= false;	
		
		Dimension canvas_size = getSize();

		// check for valid canvas sizes
		if( canvas_size.width == 0 || canvas_size.height == 0 ) return;

		//////////////////////////////////////////////////////////////
		// add vertical scroll if needed

		// remove height of horizontal scroll bar from canvas size 
		Dimension hbar_size = m_hbar.getSize();
		if( hbar_size.height != 0 )	canvas_size.height -=  hbar_size.height;
		else canvas_size.height -= 10;

		// check if we need a vertical scroll bar and add as needed
		if( m_image_height <= canvas_size.height )
		{
			if( m_vbar.isVisible() )
			{
				m_vbar.setVisible( false );
				layoutControl = true;
			}
			m_offsety = 0;
		}
		else
		{  
			int m_max_v;
			
			/*if (java_old) m_max_v = m_image_height-canvas_size.height;
			else*/ m_max_v = m_image_height;

			m_vbar.setValues( m_offsety, canvas_size.height, 0, m_max_v );
			m_vbar.setUnitIncrement(15);
			m_vbar.setBlockIncrement(canvas_size.height / 3);

			if( !m_vbar.isVisible() )
			{
				m_vbar.setVisible( true );
				layoutControl = true;
			}
		}		
		
		///////////////////////////////////////////////////////////////
		// add horizontal scroll if needed
		
		// remove width of vetical scroll bar from canvas size 
		if( m_vbar.isVisible() )
		{	
			Dimension vbar_size = m_vbar.getSize();
			if( vbar_size.width != 0 ) canvas_size.width -=  vbar_size.width;
			else canvas_size.width -= 15;
		}

		// check if we need a horizontal scroll bar and add as needed
		if( m_image_width <= canvas_size.width )
		{
			if( m_hbar.isVisible() )
			{
				m_hbar.setVisible( false );
				layoutControl = true;
			}
			m_offsetx = 0;
		}
		else
		{  
			int m_max_v;
			
			/*if (java_old) m_max_v = m_image_width - canvas_size.width;
			else*/ m_max_v = m_image_width;
			
			m_hbar.setValues( m_offsetx, canvas_size.width,  0, m_max_v );
			m_hbar.setUnitIncrement(15);
			m_hbar.setBlockIncrement( canvas_size.width / 3  );
		
			if( !m_hbar.isVisible() )
			{
				m_hbar.setVisible( true );
				layoutControl = true;
			}
		}
		
		if( layoutControl ) doLayout();
	}
	public void update( Graphics g )
	{
		Rectangle rect = new Rectangle( getSize() );
		g.setColor( m_bgColour );
		g.fillRect( rect.x, rect.y, rect.width,rect.height ); 
		paint( g );	// dont clear background
	}
}
