/*
Copyright (C) 2008 Harri Kaimio
 
This file is part of Freecine
 
Freecine is free software; you can redistribute it and/or modify it 
under the terms of the GNU General Public License as published by the Free 
Software Foundation; either version 3 of the License, or (at your option) 
any later version.
 
This program is distributed in the hope that it will be useful, but WITHOUT 
ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with 
this program; if not, see <http://www.gnu.org/licenses>.
 
Additional permission under GNU GPL version 3 section 7
 
If you modify this Program, or any covered work, by linking or combining it 
with Java Advanced Imaging (or a modified version of that library), containing 
parts covered by the terms of Java Distribution License, or leJOS, containing 
parts covered by the terms of Mozilla Public License, the licensors of this 
Program grant you additional permission to convey the resulting work. 
 */

package org.freecine.swingui;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.freecine.filmscan.FrameDescriptor;
import org.freecine.filmscan.FrameIterator;
import org.freecine.filmscan.Project;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceMap;

/**
 
 @author  harri
 */
public class MainWindow extends javax.swing.JFrame {
    
    Project prj = null;
    
    FrameIterator projectIter = null;
    
    ScanStripView stripViewer;
    
    /** Creates new form MainWindow */
    public MainWindow() {
        initComponents();
        stripViewer = (ScanStripView) stripView;
        addPropertyChangeListener( new PropertyChangeListener() {

            public void propertyChange( PropertyChangeEvent ev ) {
                System.err.println( "Peroperty " + ev.getPropertyName() + " " + ev.getOldValue() + "->" + ev.getNewValue() );
            }
        });
    }
    
    /**
     Set the project edited by this control
     @param p The new project
     */
    public void setProject( Project p ) {
        Project oldPrj = prj;
        boolean hadNextFrame = getHasNextFrame();
        boolean hadPrevFrame = getHasPrevFrame();
        this.prj = p;
        projectIter = (FrameIterator) prj.iterator();
        
        if ( projectIter.hasNext() ) {
            nextFrame();
        }
                
        firePropertyChange("project", oldPrj, prj);
        firePropertyChange("projectOpen", oldPrj != null, prj != null );
        firePropertyChange("hasNextFrame", hadNextFrame, getHasNextFrame() );
        firePropertyChange("hasPrevFrame", hadPrevFrame, getHasPrevFrame() );
        blackSlider.setValue( projectIter.getBlack() );
        whiteSlider.setValue( projectIter.getWhite() );
    }
    
    /**
     Get current project
     @return
     */
    public Project getProject() {
        return prj;
    }
    
    /**
     Utility method to check whether a project is currently open
     @return <code>true</code> if a project is open
     */
    public boolean isProjectOpen() {
        return getProject() != null;
    }
    
    /**
     Is there a frame in the project after the current one?
     @return
     */
    public boolean getHasNextFrame() {
        return ( prj != null && projectIter.hasNext() );
    }

    /**
     Is there a frame in the project before current one?
     @return
     */
    public boolean getHasPrevFrame() {
        return (prj != null && projectIter.hasPrev());
    }

    /** This method is called from within the constructor to
     initialize the form.
     WARNING: Do NOT modify this code. The content of this method is
     always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        frameViewModeGroup = new javax.swing.ButtonGroup();
        framePane = new FrameView();
        stripView = new ScanStripView();
        jPanel1 = new javax.swing.JPanel();
        blackSlider = new javax.swing.JSlider();
        jLabel4 = new javax.swing.JLabel();
        whiteSlider = new javax.swing.JSlider();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        frameNumField = new javax.swing.JTextField();
        goToFrameBtn = new javax.swing.JButton();
        nextFrameBtn = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jToolBar2 = new javax.swing.JToolBar();
        perfViewModeBtn = new javax.swing.JToggleButton();
        frameViewModeBtn = new javax.swing.JToggleButton();
        jButton1 = new javax.swing.JButton();
        jMenuBar2 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newProjectItem = new javax.swing.JMenuItem();
        openProjectItem = new javax.swing.JMenuItem();
        saveItem = new javax.swing.JMenuItem();
        saveFramesItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        exitItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        viewMenu = new javax.swing.JMenu();
        nextFrameItem = new javax.swing.JMenuItem();
        prevFrameItem = new javax.swing.JMenuItem();

        jToolBar1.setRollover(true);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Freecine");

        framePane.setBorder(new javax.swing.border.MatteBorder(null));

        javax.swing.GroupLayout framePaneLayout = new javax.swing.GroupLayout(framePane);
        framePane.setLayout(framePaneLayout);
        framePaneLayout.setHorizontalGroup(
            framePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 452, Short.MAX_VALUE)
        );
        framePaneLayout.setVerticalGroup(
            framePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 409, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout stripViewLayout = new javax.swing.GroupLayout(stripView);
        stripView.setLayout(stripViewLayout);
        stripViewLayout.setHorizontalGroup(
            stripViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 34, Short.MAX_VALUE)
        );
        stripViewLayout.setVerticalGroup(
            stripViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 488, Short.MAX_VALUE)
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Colors"));

        blackSlider.setMaximum(1000);
        blackSlider.setValue(0);
        blackSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                blackSliderStateChanged(evt);
            }
        });

        jLabel4.setText("White");

        whiteSlider.setMaximum(65535);
        whiteSlider.setValue(65535);
        whiteSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                whiteSliderStateChanged(evt);
            }
        });

        jLabel5.setText("Black");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(whiteSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                    .addComponent(blackSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(whiteSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(blackSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(268, 268, 268))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel1.setText("Frame");

        goToFrameBtn.setText("Go");
        goToFrameBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goToFrameBtnActionPerformed(evt);
            }
        });

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(org.freecine.swingui.Moviescan.class).getContext().getActionMap(MainWindow.class, this);
        nextFrameBtn.setAction(actionMap.get("nextFrame")); // NOI18N

        jButton2.setAction(actionMap.get("prevFrame")); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(frameNumField, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(goToFrameBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nextFrameBtn)
                .addContainerGap(340, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1)
                .addComponent(frameNumField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(goToFrameBtn)
                .addComponent(jButton2)
                .addComponent(nextFrameBtn))
        );

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        frameViewModeGroup.add(perfViewModeBtn);
        perfViewModeBtn.setText("Perforation");
        perfViewModeBtn.setFocusable(false);
        perfViewModeBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        perfViewModeBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        perfViewModeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                perfViewModeBtnActionPerformed(evt);
            }
        });
        jToolBar2.add(perfViewModeBtn);

        frameViewModeGroup.add(frameViewModeBtn);
        frameViewModeBtn.setSelected(true);
        frameViewModeBtn.setText("Frame");
        frameViewModeBtn.setFocusable(false);
        frameViewModeBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        frameViewModeBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        frameViewModeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                frameViewModeBtnActionPerformed(evt);
            }
        });
        jToolBar2.add(frameViewModeBtn);

        jButton1.setAction(actionMap.get("showScanDlg")); // NOI18N

        fileMenu.setText("File");

        newProjectItem.setAction(actionMap.get("createNewProject")); // NOI18N
        fileMenu.add(newProjectItem);

        openProjectItem.setAction(actionMap.get("openProject")); // NOI18N
        fileMenu.add(openProjectItem);

        saveItem.setAction(actionMap.get("saveProject")); // NOI18N
        fileMenu.add(saveItem);

        saveFramesItem.setAction(actionMap.get("showSaveFrameDlg")); // NOI18N
        fileMenu.add(saveFramesItem);
        fileMenu.add(jSeparator2);

        exitItem.setAction(actionMap.get("quit")); // NOI18N
        fileMenu.add(exitItem);

        jMenuBar2.add(fileMenu);

        editMenu.setText("Edit");
        jMenuBar2.add(editMenu);

        viewMenu.setText("View");

        nextFrameItem.setAction(actionMap.get("nextFrame")); // NOI18N
        viewMenu.add(nextFrameItem);

        prevFrameItem.setAction(actionMap.get("prevFrame")); // NOI18N
        viewMenu.add(prevFrameItem);

        jMenuBar2.add(viewMenu);

        setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(stripView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 719, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(framePane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(40, 40, 40)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(stripView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(framePane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    /**
     Move to the next frame in the project
     */
    @Action(enabledProperty = "hasNextFrame")
    public void nextFrame() {
        if ( projectIter.hasNext() ) {
            boolean hadNextFrame = getHasNextFrame();
            boolean hadPrevFrame = getHasPrevFrame();

            FrameDescriptor d = projectIter.next();
            frameNumField.setText( Integer.toString( projectIter.getCurrentFrameNum() ) );
            ((FrameView) framePane).setFrame( d );
            stripViewer.setStrip( d.getStrip() );
            stripViewer.setSelectedFrame( d.getStripFrameNum() );
            firePropertyChange( "hasNextFrame", hadNextFrame, getHasNextFrame() );
            firePropertyChange( "hasPrevFrame", hadPrevFrame, getHasPrevFrame() );
        }
    }
    
    
    
    /**
     Move to the previous frame in the project
     */
    @Action( enabledProperty="hasPrevFrame" )
    public void prevFrame() {
        if ( projectIter.hasPrev() ) {
            boolean hadNextFrame = getHasNextFrame();
            boolean hadPrevFrame = getHasPrevFrame();
            FrameDescriptor d = projectIter.prev();
            frameNumField.setText( Integer.toString( projectIter.getCurrentFrameNum() ) );
            ((FrameView) framePane).setFrame( d );
            stripViewer.setStrip( d.getStrip() );
            stripViewer.setSelectedFrame( d.getStripFrameNum() );
            firePropertyChange( "hasNextFrame", hadNextFrame, getHasNextFrame() );
            firePropertyChange( "hasPrevFrame", hadPrevFrame, getHasPrevFrame() );
        }
    }
    
    private void goToFrameBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goToFrameBtnActionPerformed
        try {
            int frame = Integer.parseInt( frameNumField.getText() );
            projectIter.setNextFrameNum( frame );
            nextFrame();
        } catch ( NumberFormatException e ) {
            JOptionPane.showMessageDialog( this,
                    "Frame number must be number", "Incorrect frame number",
                    JOptionPane.ERROR_MESSAGE );
        }
    }//GEN-LAST:event_goToFrameBtnActionPerformed

    private void whiteSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_whiteSliderStateChanged
        int val = whiteSlider.getValue();
        prj.getScene().setWhite( val );
        ((FrameView) framePane).setWhite( val );
    }//GEN-LAST:event_whiteSliderStateChanged

    private void blackSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_blackSliderStateChanged
        int val = blackSlider.getValue();
        prj.getScene().setBlack( val );
        ((FrameView) framePane).setBlack( val );
}//GEN-LAST:event_blackSliderStateChanged

    private void frameViewModeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_frameViewModeBtnActionPerformed
        ((FrameView) framePane).setMode( FrameViewMode.DRAW_FRAME );
    }//GEN-LAST:event_frameViewModeBtnActionPerformed

    private void perfViewModeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_perfViewModeBtnActionPerformed
        ((FrameView) framePane).setMode( FrameViewMode.DRAW_PERFORATION );
    }//GEN-LAST:event_perfViewModeBtnActionPerformed
    

    /**
     Show the {@link ScanProgressDlg} dialog
     */
    @Action( enabledProperty="projectOpen" )
    public void showScanDlg() {
        ScanProgressDlg dlg = new ScanProgressDlg(this, prj, false );
        dlg.setVisible(true );
    }

    
    /**
     Let user choose a directory for a new project and create it.
     */
    @Action
    public void createNewProject() {
        JFileChooser chooser = new JFileChooser();
        ApplicationContext ctxt = Application.getInstance().getContext();
        ResourceMap resource = ctxt.getResourceMap( this.getClass() );
        chooser.setDialogTitle( resource.getString("newProjectDlg.title" ) );
        chooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
        if ( chooser.showDialog( this, resource.getString("newProjectDlg.okBtn.text" ) ) == JFileChooser.APPROVE_OPTION ) {
            File projectDir = chooser.getSelectedFile();
            
            if ( !projectDir.exists() ) {
                if ( !projectDir.mkdirs() ) {
                    JOptionPane.showMessageDialog( this, "Could not create project directory",
                            "Error creting directory", JOptionPane.ERROR_MESSAGE );
                    return;
                }
            }
            if ( new File( projectDir, "project.xml" ).exists() ) {
                JOptionPane.showMessageDialog( this, "Project exists already", 
                        "Existing project", JOptionPane.ERROR_MESSAGE );
                return;
            }
            setProject( Project.getProject(projectDir) );
        }
    }

    /**
     Let the user choose a project and open it.
     */
    @Action
    public void openProject() {
        JFileChooser chooser = new JFileChooser();
        ApplicationContext ctxt = Application.getInstance().getContext();
        ResourceMap resource = ctxt.getResourceMap( this.getClass() );
        chooser.setDialogTitle( resource.getString( "openProjectDlg.title" ) );

        chooser.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES );
        chooser.setFileFilter( new FileFilter() {

            public boolean accept( File f ) {
                if ( f.isDirectory() ) {
                    return true;
                }
                if ( f.getName().equals( "project.xml" ) ) {
                    return true;
                }
                return false;
            }

            @Override
            public String getDescription() {
                return "Project files";
            }
        } );
        if ( chooser.showDialog( this, 
                resource.getString("openProjectDlg.okBtn.text" ) ) 
                == JFileChooser.APPROVE_OPTION ) {
            File projectDir = chooser.getSelectedFile();

            if ( projectDir.isDirectory() ) {
                if ( !new File( projectDir, "project.xml" ).exists() ) {
                    JOptionPane.showMessageDialog( this,
                            projectDir.getName() + " is not a project directory",
                            "No project file", JOptionPane.ERROR_MESSAGE );

                    return;

                }
            } else {
                projectDir = projectDir.getParentFile();
            }
            setProject( Project.getProject(projectDir) );
        }
    }
    
    /**
     Action to save the open project
     */
    @Action ( enabledProperty="projectOpen" )
    public void saveProject() {
        try {
            prj.save();
        } catch ( IOException ex ) {
            ApplicationContext ctxt = Application.getInstance().getContext();
            ResourceMap resource = ctxt.getResourceMap( this.getClass() );
            JOptionPane.showMessageDialog(this, 
                    resource.getString( "saveProject.Error.text" ), 
                    resource.getString( "saveProject.Error.title" ), 
                    JOptionPane.ERROR_MESSAGE );
        }
        
    }
    
    /**
     Show the {@link SaveFramesDlg}
     */
    @Action
    public void showSaveFrameDlg() {
        SaveFramesDlg dlg = new SaveFramesDlg( this, prj, true );
        dlg.setVisible( true );
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSlider blackSlider;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem exitItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JTextField frameNumField;
    private javax.swing.JPanel framePane;
    private javax.swing.JToggleButton frameViewModeBtn;
    private javax.swing.ButtonGroup frameViewModeGroup;
    private javax.swing.JButton goToFrameBtn;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JMenuItem newProjectItem;
    private javax.swing.JButton nextFrameBtn;
    private javax.swing.JMenuItem nextFrameItem;
    private javax.swing.JMenuItem openProjectItem;
    private javax.swing.JToggleButton perfViewModeBtn;
    private javax.swing.JMenuItem prevFrameItem;
    private javax.swing.JMenuItem saveFramesItem;
    private javax.swing.JMenuItem saveItem;
    private javax.swing.JPanel stripView;
    private javax.swing.JMenu viewMenu;
    private javax.swing.JSlider whiteSlider;
    // End of variables declaration//GEN-END:variables
    
}
