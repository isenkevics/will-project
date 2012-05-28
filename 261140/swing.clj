(ns swinger
  (:import (java.awt BorderLayout Container Dimension))
  (:import (javax.swing JButton JFrame JLabel JPanel JTextField JOptionPane JScrollPane JList ImageIcon JComboBox JSeparator JTable UIManager SwingUtilities AbstractButton JFileChooser JDialog JProgressBar JTabbedPane))
  (:import (javax.swing.table AbstractTableModel))
  (:import (java.awt.event MouseAdapter MouseListener KeyEvent))
  (:import (java.awt Toolkit BorderLayout Dimension Color Dialog$ModalityType))
  (:use (clojure.contrib
	  [miglayout :only (miglayout components)]
	  [swing-utils :only (add-action-listener add-key-typed-listener make-menubar)])))

(defn border-example []
  (let [frame (JFrame. "border example") pane (.getContentPane frame) ]
    (add-components-to-pane pane)
    (.pack frame)
    (.setVisible frame true)))

(defn add-components-to-pane
  [pane]
  (let [page-start (JButton. "page start")
        center     (JButton. "center")
        line-start (JButton. "line start")
        page-end   (JButton. "page end")
        line-end   (JButton. "line end") ]
    (do
      (.setComponentOrientation pane java.awt.ComponentOrientation/RIGHT_TO_LEFT)
        (doto pane
          (.add page-start BorderLayout/PAGE_START)
          (.add center BorderLayout/CENTER)
          (.add line-start BorderLayout/LINE_START)
          (.add page-end BorderLayout/PAGE_END)
          (.add line-end BorderLayout/LINE_END))
        (.setPreferredSize center (Dimension. 200 100)))))

(defn handler
  [event parent]
  (JOptionPane/showMessageDialog
    parent (str "Button " (.getActionCommand event) " clicked.")))

(defn click-me
  []
  (let [ panel (miglayout
		 (JPanel.)
		 (JButton. "Click me!") { :id :click-me }) ]
    (add-action-listener (:click-me (components panel)) handler panel)
    (doto (JFrame.)
      (.add panel)
      (.setPreferredSize (Dimension. 800 600))
      (.setResizable false)
      (.setLocationRelativeTo (:click-me (components panel)));
      (.pack)
      (.setVisible true))))

(comment (click-me))

;; JLabel emptyLabel = new JLabel("");
;; emptyLabel.setPreferredSize(new Dimension(175, 100));
;; frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);

(defn test-frame
  []
  (let [frame (JFrame. ) label (JLabel. "Hello world")]
    (.setPreferredSize frame (Dimension. 800 600))
    (.add (.getContentPane frame) label BorderLayout/CENTER)
    (.setBackground (.getContentPane frame) Color/gray)
    (.setLocation frame 300 180)
    (.pack frame)
    (.setVisible frame true)))

(comment (test-frame))

(def msg-type { :info    JOptionPane/INFORMATION_MESSAGE
		:error   JOptionPane/ERROR_MESSAGE
		:warn    JOptionPane/WARNING_MESSAGE
		:message JOptionPane/PLAIN_MESSAGE
		})

(defn- dialog
  [parent msg type]
  (JOptionPane/showMessageDialog parent msg type (msg-type (keyword type)) (ImageIcon. "/home/abhi/downloads/Firefox_wallpaper.png")))

(defn- dialog
  [parent msg type]
  (JOptionPane/showMessageDialog parent msg type (msg-type (keyword type)) ))

(defn info [parent msg]
  (dialog parent msg "info"))

(defn alert [parent msg]
  (dialog parent msg "error"))

(defn warn [parent msg]
  (dialog parent msg "warn"))

(defn message [parent msg]
  (dialog parent msg "message"))

(defn ask
  [msg]
  (let [frame (JFrame.)]
    (dialog frame msg "warn" )))

(def my-table-model
  (let [ column-names ["File" "Ranking" ]
	 column-data  [["a.txt" 10] ["b.txt" 2]]
	 row-count 5 ]
    (proxy [AbstractTableModel] []
      (getColumnCount [] (count column-names))
      (getRowCount [] row-count)
      (getValueAt [i j] (get-in column-data [i j]))
      (getColumnName [i] (column-names i)))))

(defn show-open-dialog
  [event frame]
  (JOptionPane/showMessageDialog (JPanel.)
    (let [chooser (JFileChooser.)]
      (.setFileSelectionMode chooser JFileChooser/DIRECTORIES_ONLY)
      (let [ ret (.showOpenDialog chooser frame)]
	(.setFileSelectionMode chooser JFileChooser/DIRECTORIES_ONLY)
	(str (cond
	       (= JFileChooser/APPROVE_OPTION ret) (.getSelectedFile chooser)
	       (= JFileChooser/CANCEL_OPTION ret) "canceleshwar"
	       :else "error"))))))
(defn menubar
  [frame]
  (make-menubar [{:name     "Index"
		   :mnemonic KeyEvent/VK_I
		   :items
		   [{:name "Add Directory" :mnemonic KeyEvent/VK_A :short-desc "New" :long-desc "Start " :handler (fn [_] (show-open-dialog nil frame)) }] }]))


(defn double-click? [event]
  (= 2 (.getClickCount event)))

(defn add-mouse-listener
  [component]
  (let [listener (proxy [MouseAdapter] []
                   (mouseClicked [event]
                                 (and (double-click? event) (SwingUtilities/isLeftMouseButton event)
                                      ;(info (JPanel.) (str "row " (.getAnchorSelectionIndex (.getSelectionModel component)) " selected"))
                                      (info (JPanel.) (str (.getValueAt component (.getSelectedRow component) (.getSelectedColumn  component))))
                                      )))]
    (.addMouseListener component listener)
    listener))

;; JTable code
(defn editable-table
  []
  (let [ panel (miglayout
		 (JPanel.)
		 (JScrollPane. (JTable. (to-array-2d [[1 2 3] [4 5 6] [7 8 9]])  (into-array ["a" "b" "c"])))
		 ) ]
    (doto (JFrame. "demo")
;      (.setJMenuBar menubar)
      (.add panel)
      (.setLocation 300 180)
      (.pack)
      (.setVisible true))))

(comment (editable-table))

(defn non-editable-table
  []
  (let [ jtable (JTable. my-table-model)
         panel (miglayout
                (JPanel.)
                (JScrollPane. jtable)) ]
    (add-mouse-listener jtable)
    (doto (JFrame. "demo")
;      (.setJMenuBar menubar)
      (.add panel)
      (.setLocation 300 180)
      (.pack)
      (.setVisible true))))

					; http://java.sun.com/docs/books/tutorial/uiswing/components/table.html#data
(non-editable-table)

;;  aTable.addMouseListener(new MouseAdapter(){
;;    public void mouseClicked(MouseEvent e){
;;     if (e.getClickCount() == 2){
;;        System.out.println(" double click" );
;;        }
;;     }
;;    } );
;;  }

;; http://www.rgagnon.com/javadetails/java-0336.html
;; http://java.sun.com/docs/books/tutorial/uiswing/events/mouselistener.html

;;(UIManager/setLookAndFeel (UIManager/getSystemLookAndFeelClassName))
(defn cxr-ui
  []
  (let [ jtable (JTable. my-table-model)
	 panel  (miglayout
		  (JPanel.) :column "[]"
		  (miglayout (JPanel.)) {:id :search-panel } :wrap
		  (JScrollPane. jtable) {:id :result-panel :height 400 :width 760 :gapleft 5 }
		  (miglayout (JPanel.)) {:id ::status-panel } )
	 frame (JFrame. "demo") ]
    (doto (:search-panel (components panel))
      (.add (JComboBox. (into-array (sort [" search 1" " search 2" " search 3"]))) "w 100")
      (.add (JTextField. "Enter text") "h 22, w 300, gapleft 20")
      (.add (JButton. "search") ""))
    (doto (.getColumn (.getColumnModel jtable) 1)
      (.setPreferredWidth 100)
      (.setMinWidth 100)
      (.setMaxWidth 100))
    (doto jtable
;      (.setAutoResizeMode JTable/AUTO_RESIZE_OFF) 
      (.setShowGrid false)
      (.setShowHorizontalLines true)
      (.setShowVerticalLines true)
      (.setGridColor Color/black))
    (add-mouse-listener jtable)
    (doto frame
;      (.setJMenuBar (menubar frame))
      (.setLocation 300 180)
      (.setResizable false)
      (.pack)
      (.setVisible true))))

(comment (cxr-ui))
;; http://www.apl.jhu.edu/~hall/java/Swing-Tutorial/Swing-Tutorial-JList.html
;; TODO - figure out how to fetch JTable object from (miglayout ... (JScrollPane. (JTable.)))

(defn click-me
  []
  (let [ panel (miglayout
		 (JPanel.)
		 (JButton. "Click me!") { :id :click-me }) ]
    (add-action-listener (:click-me (components panel)) show-open-dialog panel)
    (doto (JFrame.)
      (.add panel)
      (.setPreferredSize (Dimension. 400 300))
      (.setResizable false)
      (.setLocationRelativeTo (:click-me (components panel)));
      (.pack)
      (.setVisible true))))

(comment (click-me))

(defn modal
  [event parent]
  (doto (JDialog. parent "hello world" true)
    (.setPreferredSize (Dimension. 800 600))
    (.setLocation  300 180)
    (.add (JLabel. "hello world"))
    (.pack)
    (.setVisible true)))
  
  ;; (JOptionPane/showMessageDialog
  ;;   parent (str "Button " (.getActionCommand event) " clicked.")))

(defn modal-demo
  []
  (let [ panel (miglayout
		 (JPanel.)
		 (JButton. "Click me!") { :id :click-me })
	 frame (JFrame.) ]
    (add-action-listener (:click-me (components panel)) modal frame)
    (doto frame
      (.add panel)
      (.setPreferredSize (Dimension. 800 600))
      (.setResizable false)
      (.setLocationRelativeTo (:click-me (components panel)));
      (.pack)
      (.setVisible true))))

(comment (modal-demo))

;Dialog$ModalityType/DOCUMENT_MODAL

;; progress-bar
(defn progressbar
  []
  (let [ panel (miglayout (JPanel.))
	 dialog (JDialog.)
	 pbar (JProgressBar. 0 100) ]
    (.add panel pbar)
    (doto dialog
      (.setModal true)
      (.add panel)
      (.setLocationRelativeTo nil)
      (.pack)
      (.setVisible true))))

(def iwindow-table-model
  (let [ column-names ["Directory" "Indexed" ]
	 column-data  [["a.txt" "No"] ["b.txt" "Yes"]]
	 row-count 5 ]
    (proxy [AbstractTableModel] []
      (getColumnCount [] (count column-names))
      (getRowCount [] row-count)
      (getValueAt [i j] (get-in column-data [i j]))
      (getColumnName [i] (column-names i)))))

(defn iwindow
  [frame]
  (let [ jtable (JTable. iwindow-table-model)
	 panel (miglayout
		 (JPanel.)
		 (miglayout (JPanel.)) {:id :add-panel } :wrap
		 (JScrollPane. jtable) {:id :result-panel :height 400 :width 360 :gapleft 5 })
	 dialog (JDialog.)
	 button (JButton. "Add")]
    (add-action-listener button show-open-dialog panel)
    (.add (:add-panel (components panel)) button )
    (doto dialog
      (.add panel)
      (.setLocation 300 180)
      (.setLocationRelativeTo frame)
      (.pack)
      (.setVisible true))))


(defn tabbbedpane
  []
  (let [ tpane (JTabbedPane.)
	 frame (JFrame.) ]
    (.addTab tpane "a" (miglayout
			 (JPanel.)
			 (JScrollPane. (JTable. (to-array-2d [[1 2 3] [4 5 6] [7 8 9]])  (into-array ["ha" "ha" "ha"])))))
    (.addTab tpane "b" (miglayout
			 (JPanel.)
			 (JScrollPane. (JTable. (to-array-2d [[1 2 3] [4 5 6] [7 8 9]])  (into-array ["a" "b" "c"])))))
    (.setPreferredSize frame (Dimension. 800 600))
    (.add frame tpane)
    (.setVisible frame true)))

;(def column-data (ref []))
(def column-names ["Filename"])
(def file-agent (agent []))

(def running (ref nil))

(def my-dynamic-table-model
  (proxy [AbstractTableModel] []
    (getColumnCount [] (count column-names))
    (getRowCount [] (count @file-agent))
    (getValueAt [i j] (get-in @file-agent [i j]))
    (getColumnName [i] (column-names i))))

(add-watch file-agent :file-agent
  (fn [k r o n]
;    (info (JPanel.) @file-agent)
    (.fireTableRowsInserted my-dynamic-table-model 0 0)))

(defn listeshwari [x lst]
  (if (and @running (not-empty lst))
    (do (send *agent* listeshwari (rest lst))
      (Thread/sleep 1000)
      (conj @file-agent [(.getName (first lst))]))
    @file-agent))

(defn populate-table
  [event x]
  (let [dir (.getText x)]
    (info (JPanel.) dir)
    (dosync (ref-set running true))
    (send file-agent listeshwari (file-seq (java.io.File. dir)))))

(defn clear-table [event]
  (do
    (dosync
      (ref-set running false))
    (send file-agent (fn [x] []))
    (.fireTableRowsDeleted my-dynamic-table-model 0 0)))

(defn dynamic-non-editable-table
  []
  (let [ panel (miglayout
		 (JPanel.)
		 (JTextField. 20) {:id :search-key}
		 (JButton. "populate table") { :id :p-button } :span
		 (JButton. "clear table") { :id :c-button } :wrap
		 (JScrollPane. (JTable. my-dynamic-table-model)))
	 
	 {:keys [p-button c-button search-key] } (components panel) ]
    (add-action-listener p-button populate-table search-key)
    (add-action-listener c-button clear-table)
    (doto (JFrame.)
      (.add panel)
      (.setResizable false)
      (.pack)
      (.setVisible true))))

(dynamic-non-editable-table)

(comment 
  (def foo (agent nil)) ;; agent used purely for side effect
  (def bar (ref []))
  (add-watch bar :bar (fn [k r o n] (info (JPanel.) (last @bar))))
  (send foo (fn [e]
	      (do (doseq [f (file-seq (java.io.File. "/home/abhi/Docs/"))]
		    (Thread/sleep 1000)
		    (dosync 
		      (alter bar conj (.getName f))))))))

(defn change-label
  [event]
  (let [ button (.getSource event) label (.getLabel button) ]
    (.setLabel button (if (= label "on") "off" "on"))))

(defn toggle
  []
  (let [ panel (miglayout
		 (JPanel.)
		 (JButton. "on") { :id :on }) ]
    (add-action-listener (:on (components panel)) change-label)
    (doto (JFrame.)
      (.add panel)
      (.pack)
      (.setVisible true))))

;(toggle)
;(cxr-ui)
