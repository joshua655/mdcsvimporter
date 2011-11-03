// Error reading included file Templates/GUIForms/../Licenses/license-LGPL.txt
/*
 * CustomerReaderDialog.java
 *
 * Created on Aug 3, 2011, 11:49:09 PM
 */

package com.moneydance.modules.features.mdcsvimporter;

import com.moneydance.modules.features.mdcsvimporter.formats.CitiBankCanadaReader;
import com.moneydance.modules.features.mdcsvimporter.formats.CustomReader;
import com.moneydance.modules.features.mdcsvimporter.formats.INGNetherlandsReader;
import com.moneydance.modules.features.mdcsvimporter.formats.SimpleCreditDebitReader;
import com.moneydance.modules.features.mdcsvimporter.formats.WellsFargoReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

/**
 *
 * @author Stan Towianski
 */


public class CustomReaderDialog extends javax.swing.JDialog {
        
        //javax.swing.JDialog parent = null;
        ImportDialog parent = null;
        
        String [] dataTypes = { "", "ignore", "-Payment-", "-Deposit-", "date", "check number", "description", "memo" };
        String [] allowEmptyFlag = { "", "Can Be Blank" };
//        ArrayList<javax.swing.JComboBox> dataTypesList = new ArrayList<javax.swing.JComboBox>( 10 );
   //     ArrayList<javax.swing.JComboBox> emptyFlagsList = new ArrayList<javax.swing.JComboBox>( 10 );
        ArrayList<String> dataTypesList = new ArrayList<String>( 10 );
        ArrayList<String> emptyFlagsList = new ArrayList<String>( 10 );
        //ArrayList<String> dateFormatList = new ArrayList<String>( 10 );
        HashMap<String, CustomReaderData> ReaderConfigsHM = new HashMap<String, CustomReaderData>();
        HashMap<String, TransactionReader> ReaderHM = new HashMap<String, TransactionReader>();

       private static CitiBankCanadaReader citiBankCanadaReader = new CitiBankCanadaReader();
       private static INGNetherlandsReader ingNetherlandsReader = new INGNetherlandsReader();
       private static SimpleCreditDebitReader simpleCreditDebitReader = new SimpleCreditDebitReader();
       private static WellsFargoReader wellsFargoReader = new WellsFargoReader();


    /** Creates new form CustomerReaderDialog */
    public CustomReaderDialog( ImportDialog parent, boolean modal) {
        super(parent, modal);
        this.parent = parent;
        initComponents();
    }

    public boolean addReaderConfig()
        {
        message.setText( "" );
        if ( ReaderConfigsHM.containsKey( readerName.getText() ) )
            {
            message.setText( "A reader already exists by the name '" + readerName.getText() + "'" );
            return false;
            }
        
        CustomReaderData customReaderData = new CustomReaderData();
        customReaderData.setReaderName( readerName.getText() );
        customReaderData.setDataTypesList( createNewDataTypesList() );
        customReaderData.setEmptyFlagsList( createNewEmptyFlagsList() );
        //customReaderData.setDateFormatList( readDateFormatList() );
        customReaderData.setFieldSeparatorChar( getFieldSeparatorChar() );
        customReaderData.setHeaderLines( getHeaderLines() );
        customReaderData.setDateFormatString( getDateFormatString() );

        /*
        System.out.println( "add datatype===================================" );
        int i = 0;
        for ( String dataType : customReaderData.getDataTypesList() )
            {
            System.out.println( "add datatype " + i + " =" + dataType + "=" );
            i++;
            }
         */
        
        CustomReader customReader = new CustomReader( customReaderData );
        ReaderConfigsHM.put( readerName.getText(), customReaderData );
        ReaderHM.put( readerName.getText(), customReader );
        
        DefaultListModel listModel = (DefaultListModel) customReadersList.getModel();
        listModel.addElement( readerName.getText() );
        
        Settings.setCustomReaderConfig( customReaderData );
        
        this.parent.comboFileFormat1AddItem( customReader );
        
        customReader.createSupportedDateFormats( getDateFormatString() );
        this.parent.createSupportedDateFormats( getDateFormatString() );

        return true;
        }
    
    public boolean deleteReaderConfig()
        {
        message.setText( "" );
        DefaultListModel listModel = (DefaultListModel) customReadersList.getModel();
        int index = customReadersList.getSelectedIndex();
        //System.err.println( " selected index =" + index + "   item =" + listModel.getElementAt( index ) + "=" );
        
        Settings.removeCustomReaderConfig( ReaderConfigsHM.get( listModel.getElementAt( index ) ) );
        
        ReaderConfigsHM.remove( listModel.getElementAt( index ) );
        ReaderHM.remove( listModel.getElementAt( index ) );

        listModel.remove( index );
        clearReaderConfig();
        
        return true;
        }
    
    public TransactionReader getTransactionReader( String readerNameToGet )
        {
        message.setText( "" );
        if ( ! ReaderHM.containsKey( readerNameToGet ) )
            {
            message.setText( "There is no reader by that name '" + readerNameToGet + "'" );
            return null;
            }
        return ReaderHM.get( readerNameToGet );
        }
    
    public boolean getReaderConfig( String readerNameToGet )
        {
        message.setText( "" );
        if ( ! ReaderConfigsHM.containsKey( readerNameToGet ) )
            {
            message.setText( "There is no reader by that name '" + readerNameToGet + "'" );
            return false;
            }
        
        CustomReaderData customReaderData = ReaderConfigsHM.get( readerNameToGet );
        readerName.setText( readerNameToGet );
        dataTypesList = customReaderData.getDataTypesList();
        emptyFlagsList = customReaderData.getEmptyFlagsList();
        //dateFormatList = customReaderData.getDateFormatList();
        setFieldSeparatorChar( customReaderData.getFieldSeparatorChar() );
        setHeaderLines( customReaderData.getHeaderLines() );

        DefaultListModel listModel = (DefaultListModel) customReadersList.getModel();
        customReadersList.setSelectedValue( readerNameToGet, true );

        System.out.println( "get dataTypesList arraylist =" + dataTypesList + "=" );
        System.out.println( "get emptyFlagsList arraylist =" + emptyFlagsList + "=" );

        /*
        int i = 0;
//            System.out.println( "get datatype===================================" );
//            System.out.println( "get datatype===================================" );
        for ( String dataType : dataTypesList )
            {
//            System.out.println( "get datatype " + i + " =" + dataType + "=" );
            i++;
            }
         */
        
//            System.out.println( "get datatype===================================" );
        dataType0.setSelectedItem( dataTypesList.get( 0 ) );
        dataType1.setSelectedItem( dataTypesList.get( 1 ) );
        dataType2.setSelectedItem( dataTypesList.get( 2 ) );
        dataType3.setSelectedItem( dataTypesList.get( 3 ) );
        dataType4.setSelectedItem( dataTypesList.get( 4 ) );
        dataType5.setSelectedItem( dataTypesList.get( 5 ) );
        dataType6.setSelectedItem( dataTypesList.get( 6 ) );
        dataType7.setSelectedItem( dataTypesList.get( 7 ) );
        dataType8.setSelectedItem( dataTypesList.get( 8 ) );
        dataType9.setSelectedItem( dataTypesList.get( 9 ) );

        /*
        System.out.println( "get datatype===================================" );
        System.out.println( "get datatype " + i + " =" + dataTypesList.get( 0 ) + "=" );
        System.out.println( "get datatype " + i + " =" + dataTypesList.get( 1 ) + "=" );
        System.out.println( "get datatype " + i + " =" + dataTypesList.get( 2 ) + "=" );
        System.out.println( "get datatype " + i + " =" + dataTypesList.get( 3 ) + "=" );
        System.out.println( "get datatype " + i + " =" + dataTypesList.get( 4 ) + "=" );
        System.out.println( "get datatype " + i + " =" + dataTypesList.get( 5 ) + "=" );
        System.out.println( "get datatype " + i + " =" + dataTypesList.get( 6 ) + "=" );
        System.out.println( "get datatype " + i + " =" + dataTypesList.get( 7 ) + "=" );
        System.out.println( "get datatype " + i + " =" + dataTypesList.get( 8 ) + "=" );
        System.out.println( "get datatype " + i + " =" + dataTypesList.get( 9 ) + "=" );
        */
        
        isNullable0.setSelectedItem( emptyFlagsList.get( 0 ) );
        isNullable1.setSelectedItem( emptyFlagsList.get( 1 ) );
        isNullable2.setSelectedItem( emptyFlagsList.get( 2 ) );
        isNullable3.setSelectedItem( emptyFlagsList.get( 3 ) );
        isNullable4.setSelectedItem( emptyFlagsList.get( 4 ) );
        isNullable5.setSelectedItem( emptyFlagsList.get( 5 ) );
        isNullable6.setSelectedItem( emptyFlagsList.get( 6 ) );
        isNullable7.setSelectedItem( emptyFlagsList.get( 7 ) );
        isNullable8.setSelectedItem( emptyFlagsList.get( 8 ) );
        isNullable9.setSelectedItem( emptyFlagsList.get( 9 ) );
                
        /*
        DefaultComboBoxModel dateFormatModel = new DefaultComboBoxModel();
        for ( String format : dateFormatList )
            {
            System.out.println( "add date format =" + format + "=" );
            dateFormatModel.addElement( format );
            }
        
        dateFormatCB.setModel( dateFormatModel );
            if ( this.parent != null )
                {
                this.parent.comboDateFormatSetModel( dateFormatModel );
                }
        
        TransactionReader customReader = ReaderHM.get( readerName.getText() );
        String [] tmpArray = new String[0];
        customReader.setSupportedDateFormats( dateFormatList.toArray( tmpArray ) );
         */
        
        CustomReader customReader = (CustomReader) ReaderHM.get( readerName.getText() );
        setDateFormatString( customReaderData.getDateFormatString() );

        customReader.createSupportedDateFormats( getDateFormatString() );
        this.parent.createSupportedDateFormats( getDateFormatString() );

        System.err.println( "getNumberOfCustomReaderFieldsUsed() =" + getNumberOfCustomReaderFieldsUsed() );
        return true;
        }
    
    public void clearReaderConfig()
        {
        setFieldSeparatorChar( ',' );
        setHeaderLines( 1 );
        
        dataType0.setSelectedIndex( 0 );
        dataType1.setSelectedIndex( 0 );
        dataType2.setSelectedIndex( 0 );
        dataType3.setSelectedIndex( 0 );
        dataType4.setSelectedIndex( 0 );
        dataType5.setSelectedIndex( 0 );
        dataType6.setSelectedIndex( 0 );
        dataType7.setSelectedIndex( 0 );
        dataType8.setSelectedIndex( 0 );
        dataType9.setSelectedIndex( 0 );
        
        isNullable0.setSelectedIndex( 0 );
        isNullable1.setSelectedIndex( 0 );
        isNullable2.setSelectedIndex( 0 );
        isNullable3.setSelectedIndex( 0 );
        isNullable4.setSelectedIndex( 0 );
        isNullable5.setSelectedIndex( 0 );
        isNullable6.setSelectedIndex( 0 );
        isNullable7.setSelectedIndex( 0 );
        isNullable8.setSelectedIndex( 0 );
        isNullable9.setSelectedIndex( 0 );
        }

    public ArrayList<String> createNewDataTypesList() 
        {
        ArrayList<String> newDataTypesList = new ArrayList<String>( 10 );
//        Collections.copy( newDataTypesList, dataTypesList );
        
        newDataTypesList.add( ((String)dataType0.getSelectedItem()));
        newDataTypesList.add( ((String)dataType1.getSelectedItem()));
        newDataTypesList.add( ((String)dataType2.getSelectedItem()));
        newDataTypesList.add( ((String)dataType3.getSelectedItem()));
        newDataTypesList.add( ((String)dataType4.getSelectedItem()));
        newDataTypesList.add( ((String)dataType5.getSelectedItem()));
        newDataTypesList.add( ((String)dataType6.getSelectedItem()));
        newDataTypesList.add( ((String)dataType7.getSelectedItem()));
        newDataTypesList.add( ((String)dataType8.getSelectedItem()));
        newDataTypesList.add( ((String)dataType9.getSelectedItem()));        
                
//        for ( int i = 0; i < 10; i ++ )
//            {
//            newDataTypesList.add( new String( dataTypesList.get( i ) ) );
//            }
        return newDataTypesList;
        }

    public ArrayList<String> createNewEmptyFlagsList() 
        {
        ArrayList<String> newEmptyFlagsList = new ArrayList<String>( 10 );
        newEmptyFlagsList.add( ((String)isNullable0.getSelectedItem()));
        newEmptyFlagsList.add( ((String)isNullable1.getSelectedItem()));
        newEmptyFlagsList.add( ((String)isNullable2.getSelectedItem()));
        newEmptyFlagsList.add( ((String)isNullable3.getSelectedItem()));
        newEmptyFlagsList.add( ((String)isNullable4.getSelectedItem()));
        newEmptyFlagsList.add( ((String)isNullable5.getSelectedItem()));
        newEmptyFlagsList.add( ((String)isNullable6.getSelectedItem()));
        newEmptyFlagsList.add( ((String)isNullable7.getSelectedItem()));
        newEmptyFlagsList.add( ((String)isNullable8.getSelectedItem()));
        newEmptyFlagsList.add( ((String)isNullable9.getSelectedItem()));        
        return newEmptyFlagsList;
        }

    /*
    public ArrayList<String> readDateFormatList() 
        {
        DefaultComboBoxModel dcbm = (DefaultComboBoxModel) dateFormatCB.getModel();
        
        int max = dcbm.getSize();
        ArrayList<String> newList = new ArrayList<String>( max );
        for ( int i = 0; i < max; i++ )
            {
            newList.add( ( (String) dcbm.getElementAt( i ) ) );
            }
        return newList;
        }
    */
    /*
    public ArrayList<javax.swing.JComboBox> createNewEmptyFlagsList() 
        {
        ArrayList<javax.swing.JComboBox> newEmptyFlagsList = new ArrayList<javax.swing.JComboBox>( 10 );
        for ( int i = 0; i < 10; i ++ )
            {
            javax.swing.JComboBox jcb = new javax.swing.JComboBox( new javax.swing.DefaultComboBoxModel( allowEmptyFlag ) );
            jcb.setSelectedItem( emptyFlagsList.get( i ).getSelectedItem() );
            newEmptyFlagsList.add( jcb );
            }
        return newEmptyFlagsList;
        }
     */
    
    public String getDataTypesListSelectedItem( int index ) {
        return (String) dataTypesList.get( index );
    }

    public String getEmptyFlagsListSelectedItem( int index ) {
        return (String) emptyFlagsList.get( index );
    }

    public String getDateFormatSelected() {
        //return (String) dateFormatCB.getSelectedItem();
        return (String) dateFormatCr.getText();
    }
    
    public void setDateFormatString( String xxx) {
        //dateFormatCB.setSelectedItem( xxx );
        dateFormatCr.setText( xxx );
    }

    public String getDateFormatString() {
        //return (String) dateFormatCB.getSelectedItem();
        return dateFormatCr.getText();
    }

    public int getNumberOfCustomReaderFieldsUsed()
        {
        int c = 0;
        int max = dataTypesList.size();
        
        for (       ; c < max; c++ )
            {
            //System.err.println( "(String) dataTypesList.get(" + c + ") =" + (String) dataTypesList.get( c ) + "=" );
            if ( ((String) dataTypesList.get( c )).equalsIgnoreCase( "" ) )
                return c;
            }
        return c;
        }

    public void setHeaderLines( int xxx ) 
        {
        headerLines.setText( String.valueOf( xxx ) );
        }
    
    public int getHeaderLines() {
        int x = 0;
        try
            {
            x = Integer.parseInt( headerLines.getText().trim() );
            }
        catch ( Exception ex )
            {
            ;
            }
        return x;
    }

    public void setFieldSeparatorChar( int xxx) {
        fieldSeparatorChar.setText( String.valueOf( Character.toString( (char) xxx ) ) );
    }

    public int getFieldSeparatorChar() {
        return fieldSeparatorChar.getText().charAt( 0 );
    }
    
    protected void init()
        {
        /*
        dataType0.setSelectedItem( "date" );
        dataType1.setSelectedItem( "amount" );
        dataType2.setSelectedItem( "check number" );
        dataType3 .setSelectedItem( "skip" );
        dataType4.setSelectedItem( "description" );
        dataType5.setSelectedItem( "memo" );
        isNullable2.setSelectedItem( "Can Be Blank" );
        isNullable4.setSelectedItem( "Can Be Blank" );
        isNullable5.setSelectedItem( "Can Be Blank" );
        */
        
        ReaderConfigsHM = Settings.createReaderConfigsHM();
        ReaderHM = Settings.getReaderHM();  // not great, but for now the call above must be first because it sets the value for this one to read also.

        ReaderConfigsHM.put( "citiBankCanadaReader", null );
        ReaderHM.put( "citiBankCanadaReader", citiBankCanadaReader );
        
        ReaderConfigsHM.put( "ingNetherlandsReader", null );
        ReaderHM.put( "ingNetherlandsReader", ingNetherlandsReader );
        
        ReaderConfigsHM.put( "simpleCreditDebitReader", null );
        ReaderHM.put( "simpleCreditDebitReader", simpleCreditDebitReader );
        
        ReaderConfigsHM.put( "wellsFargoReader", null );
        ReaderHM.put( "wellsFargoReader", wellsFargoReader );
        
        DefaultListModel listModel = (DefaultListModel) customReadersList.getModel();

// ???        this.parent.comboFileFormat1AddItem( "" );

        // For keys of a map
        for ( Iterator it=ReaderHM.keySet().iterator(); it.hasNext(); ) 
            {
            String readerName = (String) it.next();
            System.out.println( "fill out readerName =" + readerName + "=" );
            if ( ReaderHM.get( readerName ).isCustomReaderFlag() )
                {
                listModel.addElement( readerName );
                
                // needs to be in Settings()
//                CustomReader customReader = (CustomReader) getTransactionReader( readerName );
//                CustomReaderData customReaderData = ReaderConfigsHM.get( readerName );
//
//                customReader.createSupportedDateFormats( customReaderData.getDateFormatString() );
                }
            if ( this.parent != null )
                {
                System.out.println( "call add readerName to import dlg reader list =" + readerName + "=" );
                this.parent.comboFileFormat1AddItem( ReaderHM.get( readerName ) );
                }
            }
        
        dataTypesList.add( (String)dataType0.getSelectedItem() );
        dataTypesList.add( (String)dataType1.getSelectedItem() );
        dataTypesList.add( (String)dataType2.getSelectedItem() );
        dataTypesList.add( (String)dataType3.getSelectedItem() );
        dataTypesList.add( (String)dataType4.getSelectedItem() );
        dataTypesList.add( (String)dataType5.getSelectedItem() );
        dataTypesList.add( (String)dataType6.getSelectedItem() );
        dataTypesList.add( (String)dataType7.getSelectedItem() );
        dataTypesList.add( (String)dataType8.getSelectedItem() );
        dataTypesList.add( (String)dataType9.getSelectedItem() );
        emptyFlagsList.add( (String)isNullable0.getSelectedItem() );
        emptyFlagsList.add( (String)isNullable1.getSelectedItem() );
        emptyFlagsList.add( (String)isNullable2.getSelectedItem() );
        emptyFlagsList.add( (String)isNullable3.getSelectedItem() );
        emptyFlagsList.add( (String)isNullable4.getSelectedItem() );
        emptyFlagsList.add( (String)isNullable5.getSelectedItem() );
        emptyFlagsList.add( (String)isNullable6.getSelectedItem() );
        emptyFlagsList.add( (String)isNullable7.getSelectedItem() );
        emptyFlagsList.add( (String)isNullable8.getSelectedItem() );
        emptyFlagsList.add( (String)isNullable9.getSelectedItem() );

    }                                 
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        readerName = new javax.swing.JTextField();
        dataType1 = new javax.swing.JComboBox();
        isNullable1 = new javax.swing.JComboBox();
        dataType2 = new javax.swing.JComboBox();
        dataType4 = new javax.swing.JComboBox();
        dataType0 = new javax.swing.JComboBox();
        isNullable0 = new javax.swing.JComboBox();
        isNullable2 = new javax.swing.JComboBox();
        isNullable3 = new javax.swing.JComboBox();
        isNullable4 = new javax.swing.JComboBox();
        isNullable5 = new javax.swing.JComboBox();
        isNullable6 = new javax.swing.JComboBox();
        isNullable7 = new javax.swing.JComboBox();
        isNullable8 = new javax.swing.JComboBox();
        isNullable9 = new javax.swing.JComboBox();
        dataType3 = new javax.swing.JComboBox();
        dataType5 = new javax.swing.JComboBox();
        dataType6 = new javax.swing.JComboBox();
        dataType7 = new javax.swing.JComboBox();
        dataType8 = new javax.swing.JComboBox();
        dataType9 = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        saveBtn = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        headerLines = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        customReadersList = new javax.swing.JList();
        jLabel14 = new javax.swing.JLabel();
        addBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        message = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        doneBtn = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        fieldSeparatorChar = new javax.swing.JTextField();
        resetFieldsBtn = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        dateFormatCr = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(70, 20));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Reader Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        getContentPane().add(jLabel1, gridBagConstraints);

        readerName.setPreferredSize(new java.awt.Dimension(160, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        getContentPane().add(readerName, gridBagConstraints);

        dataType1.setModel(new javax.swing.DefaultComboBoxModel( dataTypes ) );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        getContentPane().add(dataType1, gridBagConstraints);

        isNullable1.setModel(new javax.swing.DefaultComboBoxModel( allowEmptyFlag ) );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        getContentPane().add(isNullable1, gridBagConstraints);

        dataType2.setModel(new javax.swing.DefaultComboBoxModel( dataTypes ) );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        getContentPane().add(dataType2, gridBagConstraints);

        dataType4.setModel(new javax.swing.DefaultComboBoxModel( dataTypes ) );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        getContentPane().add(dataType4, gridBagConstraints);

        dataType0.setModel(new javax.swing.DefaultComboBoxModel( dataTypes ) );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        getContentPane().add(dataType0, gridBagConstraints);

        isNullable0.setModel(new javax.swing.DefaultComboBoxModel( allowEmptyFlag ) );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        getContentPane().add(isNullable0, gridBagConstraints);

        isNullable2.setModel(new javax.swing.DefaultComboBoxModel( allowEmptyFlag ) );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        getContentPane().add(isNullable2, gridBagConstraints);

        isNullable3.setModel(new javax.swing.DefaultComboBoxModel( allowEmptyFlag ) );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        getContentPane().add(isNullable3, gridBagConstraints);

        isNullable4.setModel(new javax.swing.DefaultComboBoxModel( allowEmptyFlag ) );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        getContentPane().add(isNullable4, gridBagConstraints);

        isNullable5.setModel(new javax.swing.DefaultComboBoxModel( allowEmptyFlag ) );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        getContentPane().add(isNullable5, gridBagConstraints);

        isNullable6.setModel(new javax.swing.DefaultComboBoxModel( allowEmptyFlag ) );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        getContentPane().add(isNullable6, gridBagConstraints);

        isNullable7.setModel(new javax.swing.DefaultComboBoxModel( allowEmptyFlag ) );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        getContentPane().add(isNullable7, gridBagConstraints);

        isNullable8.setModel(new javax.swing.DefaultComboBoxModel( allowEmptyFlag ) );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        getContentPane().add(isNullable8, gridBagConstraints);

        isNullable9.setModel(new javax.swing.DefaultComboBoxModel( allowEmptyFlag ) );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        getContentPane().add(isNullable9, gridBagConstraints);

        dataType3.setModel(new javax.swing.DefaultComboBoxModel( dataTypes ) );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        getContentPane().add(dataType3, gridBagConstraints);

        dataType5.setModel(new javax.swing.DefaultComboBoxModel( dataTypes ) );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        getContentPane().add(dataType5, gridBagConstraints);

        dataType6.setModel(new javax.swing.DefaultComboBoxModel( dataTypes ) );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        getContentPane().add(dataType6, gridBagConstraints);

        dataType7.setModel(new javax.swing.DefaultComboBoxModel( dataTypes ) );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 12;
        getContentPane().add(dataType7, gridBagConstraints);

        dataType8.setModel(new javax.swing.DefaultComboBoxModel( dataTypes ) );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 13;
        getContentPane().add(dataType8, gridBagConstraints);

        dataType9.setModel(new javax.swing.DefaultComboBoxModel( dataTypes ) );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 14;
        getContentPane().add(dataType9, gridBagConstraints);

        jLabel2.setText("1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        getContentPane().add(jLabel2, gridBagConstraints);

        jLabel3.setText("2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        getContentPane().add(jLabel3, gridBagConstraints);

        jLabel4.setText("3");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        getContentPane().add(jLabel4, gridBagConstraints);

        jLabel5.setText("4");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        getContentPane().add(jLabel5, gridBagConstraints);

        jLabel6.setText("5");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        getContentPane().add(jLabel6, gridBagConstraints);

        jLabel7.setText("6");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        getContentPane().add(jLabel7, gridBagConstraints);

        jLabel8.setText("7");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        getContentPane().add(jLabel8, gridBagConstraints);

        jLabel9.setText("8");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        getContentPane().add(jLabel9, gridBagConstraints);

        jLabel10.setText("9");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        getContentPane().add(jLabel10, gridBagConstraints);

        jLabel11.setText("10");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        getContentPane().add(jLabel11, gridBagConstraints);

        saveBtn.setText("Save");
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 16;
        getContentPane().add(saveBtn, gridBagConstraints);

        jLabel12.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 15;
        getContentPane().add(jLabel12, gridBagConstraints);

        jLabel13.setText("Number of Header Lines:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        getContentPane().add(jLabel13, gridBagConstraints);

        headerLines.setText("1");
        headerLines.setPreferredSize(new java.awt.Dimension(40, 19));
        headerLines.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                headerLinesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        getContentPane().add(headerLines, gridBagConstraints);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(160, 160));

        customReadersList.setModel(new DefaultListModel() );
        customReadersList.setMaximumSize(new java.awt.Dimension(80, 85));
        customReadersList.setMinimumSize(new java.awt.Dimension(80, 85));
        customReadersList.setPreferredSize(new java.awt.Dimension(100, 85));
        customReadersList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                customReadersListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(customReadersList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jScrollPane1, gridBagConstraints);

        jLabel14.setText(" ");
        jLabel14.setMaximumSize(new java.awt.Dimension(25, 15));
        jLabel14.setMinimumSize(new java.awt.Dimension(25, 15));
        jLabel14.setPreferredSize(new java.awt.Dimension(25, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        getContentPane().add(jLabel14, gridBagConstraints);

        addBtn.setText("Add");
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 16;
        getContentPane().add(addBtn, gridBagConstraints);

        deleteBtn.setText("Delete");
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 16;
        getContentPane().add(deleteBtn, gridBagConstraints);

        jLabel15.setText("List of Readers:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        getContentPane().add(jLabel15, gridBagConstraints);

        message.setForeground(new java.awt.Color(255, 0, 0));
        message.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.gridwidth = 5;
        getContentPane().add(message, gridBagConstraints);

        jLabel16.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 18;
        getContentPane().add(jLabel16, gridBagConstraints);

        jLabel17.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        getContentPane().add(jLabel17, gridBagConstraints);

        doneBtn.setText("Done");
        doneBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doneBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 16;
        getContentPane().add(doneBtn, gridBagConstraints);

        jLabel18.setText("Field Separator:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        getContentPane().add(jLabel18, gridBagConstraints);

        fieldSeparatorChar.setText(",");
        fieldSeparatorChar.setPreferredSize(new java.awt.Dimension(20, 19));
        fieldSeparatorChar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldSeparatorCharActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        getContentPane().add(fieldSeparatorChar, gridBagConstraints);

        resetFieldsBtn.setText("Reset Fields");
        resetFieldsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetFieldsBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        getContentPane().add(resetFieldsBtn, gridBagConstraints);

        jLabel19.setText("Date Format:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        getContentPane().add(jLabel19, gridBagConstraints);

        dateFormatCr.setText("YYYY-MM-DD");
        dateFormatCr.setToolTipText("<html>\nYou can do things like: MM/DD/YYYY, MM.DD.YY, YY-MM-DD<br/>\nhttp://download.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html");
        dateFormatCr.setMinimumSize(new java.awt.Dimension(100, 19));
        dateFormatCr.setPreferredSize(new java.awt.Dimension(100, 19));
        dateFormatCr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateFormatCrActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        getContentPane().add(dateFormatCr, gridBagConstraints);

        jLabel20.setText("             ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        getContentPane().add(jLabel20, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        message.setText( "" );

        if ( ! ReaderConfigsHM.containsKey( readerName.getText() ) )
            {
            message.setText( "There is no reader by that name '" + readerName.getText() + "'" );
            }
        
//        int i = 0;
//        for ( String dataType : dataTypesList )
//            {
//            System.out.println( "datatype " + i + " =" + dataType + "=" );
//            i++;
//            }

        try
            {
            int x = Integer.parseInt( headerLines.getText().trim() );
            if ( x < 0 )
                throw new Exception();
            }
        catch ( Exception ex )
            {
            message.setText( "Number of Header Lines must be 0 or more" );
            return;
            }
        
        CustomReaderData customReaderData = ReaderConfigsHM.get( readerName.getText() );
        
        customReaderData.setReaderName( readerName.getText() );
        customReaderData.setDataTypesList( createNewDataTypesList() );
        customReaderData.setEmptyFlagsList( createNewEmptyFlagsList() );
        //customReaderData.setDateFormatList( readDateFormatList() );
        customReaderData.setFieldSeparatorChar( getFieldSeparatorChar() );
        customReaderData.setHeaderLines( getHeaderLines() );
        customReaderData.setDateFormatString( getDateFormatString() );
        
        ReaderConfigsHM.put( readerName.getText(), customReaderData );
        // *** I could get and replace the existing one but just do this for now until things work  ! ! !
        CustomReader customReader = new CustomReader( customReaderData );
        ReaderHM.put( readerName.getText(), customReader );

        customReader.createSupportedDateFormats( getDateFormatString() );
        this.parent.createSupportedDateFormats( getDateFormatString() );

        Settings.setCustomReaderConfig( customReaderData );
    }//GEN-LAST:event_saveBtnActionPerformed

    private void headerLinesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_headerLinesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_headerLinesActionPerformed

    private void doneBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doneBtnActionPerformed
        this.setVisible( false );
        parent.setSkipDuringInit( true );
        parent.fileChanged();
        this.parent.comboFileFormat1SetItem( ReaderHM.get( readerName.getText() ) );
        parent.setSkipDuringInit( false );
        //System.out.println( "done button  (String) dateFormatCB.getSelectedItem() =" + (String) dateFormatCB.getSelectedItem() + "=" );
        //this.parent.comboDateFormatSetItem( getDateFormatString() );
        this.parent.createSupportedDateFormats( getDateFormatString() );

    }//GEN-LAST:event_doneBtnActionPerformed

    private void fieldSeparatorCharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldSeparatorCharActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldSeparatorCharActionPerformed

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        addReaderConfig();
    }//GEN-LAST:event_addBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        deleteReaderConfig();
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void customReadersListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customReadersListMouseClicked
        DefaultListModel listModel = (DefaultListModel) customReadersList.getModel();
        int index = customReadersList.getSelectedIndex();
        getReaderConfig( (String) listModel.getElementAt( index ) );
    }//GEN-LAST:event_customReadersListMouseClicked

    private void resetFieldsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetFieldsBtnActionPerformed
        clearReaderConfig();
    }//GEN-LAST:event_resetFieldsBtnActionPerformed

    private void dateFormatCrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateFormatCrActionPerformed
            ;
    }//GEN-LAST:event_dateFormatCrActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                CustomReaderDialog dialog = new CustomReaderDialog( null, true );
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn;
    private javax.swing.JList customReadersList;
    private javax.swing.JComboBox dataType0;
    private javax.swing.JComboBox dataType1;
    private javax.swing.JComboBox dataType2;
    private javax.swing.JComboBox dataType3;
    private javax.swing.JComboBox dataType4;
    private javax.swing.JComboBox dataType5;
    private javax.swing.JComboBox dataType6;
    private javax.swing.JComboBox dataType7;
    private javax.swing.JComboBox dataType8;
    private javax.swing.JComboBox dataType9;
    private javax.swing.JTextField dateFormatCr;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton doneBtn;
    private javax.swing.JTextField fieldSeparatorChar;
    private javax.swing.JTextField headerLines;
    private javax.swing.JComboBox isNullable0;
    private javax.swing.JComboBox isNullable1;
    private javax.swing.JComboBox isNullable2;
    private javax.swing.JComboBox isNullable3;
    private javax.swing.JComboBox isNullable4;
    private javax.swing.JComboBox isNullable5;
    private javax.swing.JComboBox isNullable6;
    private javax.swing.JComboBox isNullable7;
    private javax.swing.JComboBox isNullable8;
    private javax.swing.JComboBox isNullable9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel message;
    private javax.swing.JTextField readerName;
    private javax.swing.JButton resetFieldsBtn;
    private javax.swing.JButton saveBtn;
    // End of variables declaration//GEN-END:variables
}
