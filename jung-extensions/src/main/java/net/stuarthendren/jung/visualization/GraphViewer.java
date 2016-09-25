package net.stuarthendren.jung.visualization;

import java.awt.Color;
import java.awt.Paint;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import org.apache.commons.collections4.Transformer;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.util.Animator;

/**
 * Graph viewer shows the graph with basic control
 * 
 * Applies {@link FRLayout} and {@link DefaultModalGraphMouse}.
 * 
 * <pre>
 * Keys:
 * 		MouseMode 
 * 			Picking 'p' 
 * 			Transforming 't'
 * 		Layout
 * 			f {@link FRLayout}
 * 			s {@link SpringLayout}
 * </pre>
 * 
 * @author Stuart Hendren
 * 
 */
public class GraphViewer {

	public static <V, E> void show(final Graph<V, E> graph) {
		JFrame jf = new JFrame();
		final VisualizationViewer<V, E> vv = new VisualizationViewer<V, E>(new FRLayout<V, E>(graph));
		vv.setBackground(Color.white);
		vv.getRenderContext().setVertexDrawPaintTransformer(new Transformer<V, Paint>() {
			@Override
			public Paint transform(V v) {
				if (vv.getPickedVertexState().isPicked(v)) {
					return Color.cyan;
				} else {
					return Color.black;
				}
			}
		});
		DefaultModalGraphMouse<V, E> gm = new DefaultModalGraphMouse<V, E>();
		vv.setGraphMouse(gm);
		vv.addKeyListener(gm.getModeKeyListener());
		vv.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char keyChar = e.getKeyChar();
				if (keyChar == 's') {
					changeLayout(vv, new SpringLayout<V, E>(graph));
				} else if (keyChar == 'f') {
					changeLayout(vv, new FRLayout<V, E>(graph));

				}

			}

		});
		jf.getContentPane().add(vv);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.pack();
		jf.setVisible(true);
	}

	private static <V, E> void changeLayout(VisualizationViewer<V, E> vv, Layout<V, E> l) {
		l.setInitializer(vv.getGraphLayout());
		l.setSize(vv.getSize());

		LayoutTransition<V, E> lt = new LayoutTransition<V, E>(vv, vv.getGraphLayout(), l);
		Animator animator = new Animator(lt);
		animator.start();
		vv.getRenderContext().getMultiLayerTransformer().setToIdentity();
		vv.repaint();
	}

}
