package ba.unsa.etf.si.tim1.jKP;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.*;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.interactive.action.type.PDActionURI;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PagePanel;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import java.awt.GridLayout;

public class Izvjestaji extends JPanel {
	private JComboBox<String> comboBox;
	public Izvjestaji()  {
		this.setBounds(201, 0, 990, 800);
		this.setBackground(Color.white);
		this.setLayout(null);
		
        JLabel lblTipIzvjetaja = new JLabel("Tip izvještaja:");
        lblTipIzvjetaja.setBounds(39, 28, 87, 15);
        lblTipIzvjetaja.setHorizontalTextPosition(JLabel.RIGHT);
        this.add(lblTipIzvjetaja);
        
        comboBox = new JComboBox<String>();
        comboBox.setBounds(144, 23, 490, 24);
        comboBox.addItem("Sedmični pregled rada zaposlenika");
        comboBox.addItem("Mjesečni izvještaj o radnim nalozima");
        comboBox.addItem("Mjesečni izvještaj o storniranim radnim nalozima");
        comboBox.addItem("Mjesečni izvještaj o lokacijama s više intervencija");
        comboBox.addItem("Godišnji sumarni izvještaj o radnim nalozima");
        comboBox.setSelectedIndex(4);
        this.add(comboBox);

        final JLabel lblDoDatuma = new JLabel("Do datuma:");
        lblDoDatuma.setBounds(51, 69, 75, 15);
        this.add(lblDoDatuma);
        
        UtilDateModel model = new UtilDateModel();
                JDatePanelImpl datePanel = new JDatePanelImpl(model);
        
        final JLabel mjesec = new JLabel("Mjesec:");
        mjesec.setBounds(71, 67, 75, 15);
        add(mjesec);
        
        final JComboBox comboBox_1 = new JComboBox();
        comboBox_1.setBounds(144, 64, 80, 20);
        add(comboBox_1);
        comboBox_1.addItem("Januar"); comboBox_1.addItem("Februar"); comboBox_1.addItem("Mart"); comboBox_1.addItem("April"); 
        comboBox_1.addItem("Maj"); comboBox_1.addItem("Juni"); comboBox_1.addItem("Juli"); comboBox_1.addItem("August"); 
        comboBox_1.addItem("Septembar"); comboBox_1.addItem("Oktobar"); comboBox_1.addItem("Novembar"); comboBox_1.addItem("Decembar");
        
        final JComboBox comboBox_2 = new JComboBox();
        comboBox_2.setBounds(318, 64, 61, 20);
        add(comboBox_2);
        comboBox_2.addItem("2014");
        
        final JLabel godina = new JLabel("Godina:");
        godina.setBounds(259, 67, 37, 14);
        add(godina);
        
        JButton btnOk = new JButton("OK");
        btnOk.setBounds(389, 65, 75, 23);
        add(btnOk);
        final JDatePickerImpl datePicker = new JDatePickerImpl(datePanel);
        datePicker.setLocation(144, 59);
        datePicker.setSize(202, 26);
        this.add(datePicker);
        final PagePanel prikazPdf = new PagePanel();
      //prikazPdf.showPage(page);   
        final JScrollPane a = new JScrollPane(prikazPdf);
        a.setBounds(20, 130, 950, 420);
        a.setVisible(true);
        this.add(a);
        mjesec.setVisible(false);
        godina.setVisible(false);
		comboBox_1.setVisible(false);
		comboBox_2.setVisible(false);
        try {
        	comboBox.addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent e) {
        			if(comboBox.getSelectedIndex() == 1 || comboBox.getSelectedIndex() == 2 || comboBox.getSelectedIndex() == 3) {
                		datePicker.setVisible(false);
                		lblDoDatuma.setVisible(false);
                		mjesec.setVisible(true);
                		comboBox_1.setVisible(true);
                		comboBox_2.setVisible(true);
                		godina.setVisible(true);
        			}
        			if(comboBox.getSelectedIndex() == 0) {
                		datePicker.setVisible(true);
                		lblDoDatuma.setVisible(true);
                		mjesec.setVisible(false);
                		comboBox_1.setVisible(false);
                		comboBox_2.setVisible(false);
                		godina.setVisible(false);
        			}
        		}
        		
        	});
        	btnOk.addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent e) {
        			File file1 = new File("izvjestaj.pdf");
        			file1.delete();
        			RandomAccessFile raf;
        			File file = new File("");
        			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        			Date date = new Date();
        			if(comboBox.getSelectedIndex() == 0) {
        				file = new File(dateFormat.format(date));
                		SedmicniRadnici(dateFormat.format(date), (Date) datePicker.getModel().getValue());
                	}
        			if(comboBox.getSelectedIndex() == 1) {
        				file = new File(dateFormat.format(date));
                		try {
							MjesecniSumarni(dateFormat.format(date), comboBox_1.getSelectedIndex() + 1, comboBox_2.getSelectedItem().toString());
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(getRootPane(), e1.getMessage());
						}
                	}
        			if(comboBox.getSelectedIndex() == 2) {
        				file = new File(dateFormat.format(date));
                		try {
							MjesecniStornirani(dateFormat.format(date), comboBox_1.getSelectedIndex() + 1, comboBox_2.getSelectedItem().toString());
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(getRootPane(), e1.getMessage());
						}
                	}
                	
        			

                   
                    FileChannel channel; 
                    ByteBuffer buf;
                    PDFFile pdffile;
                    final PDFPage page;
                    try {
						raf = new RandomAccessFile(file, "r");
						channel = raf.getChannel();
	        			buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
	        			pdffile = new PDFFile(buf);
	        			page = pdffile.getPage(1); 
	        	        prikazPdf.useZoomTool(true);
	        	        prikazPdf.showPage(page);
	        	        raf.close();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
        		}
        	});
			datePicker.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	
                }
			});
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        //donja dugmad
        JButton jSpasiti = new JButton("Spasiti");
        jSpasiti.setBounds(650, 600, 153, 48);
        jSpasiti.setBackground(Color.LIGHT_GRAY);
        this.add(jSpasiti);
        jSpasiti.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		JOptionPane.showMessageDialog(getRootPane(), "Nije implementirano!");
        	}
        });
        
        JButton jStampati = new JButton("Odštampati");
        jStampati.setBounds(810, 600, 153, 48);
        jStampati.setBackground(Color.LIGHT_GRAY);
        this.add(jStampati);
        jStampati.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		JOptionPane.showMessageDialog(getRootPane(), "Nije implementirano!");
        	}
        });
       
	}
	public static void drawTable(PDPage page, PDPageContentStream contentStream, 
            float y, float margin, String[][] content) throws IOException {
			final int rows = content.length;
			final int cols = content[0].length;
			final float rowHeight = 20f;
			final float tableWidth = page.findMediaBox().getWidth() - margin - margin;
			final float tableHeight = rowHeight * rows;
			final float colWidth = tableWidth/(float)cols;
			final float cellMargin=5f;
			
			//draw the rows
			float nexty = y ;
			for (int i = 0; i <= rows; i++) {
				contentStream.drawLine(margin, nexty, margin+tableWidth, nexty);
				nexty-= rowHeight;
			}
			
			//draw the columns
			float nextx = margin;
			for (int i = 0; i <= cols; i++) {
				contentStream.drawLine(nextx, y, nextx, y-tableHeight);
				nextx += colWidth;
			}
			
			//now add the text        
			contentStream.setFont( PDType1Font.TIMES_ROMAN , 12 );    
			
			float textx = margin+cellMargin;
			float texty = y-15;        
			for(int i = 0; i < content.length; i++){
				for(int j = 0 ; j < content[i].length; j++){
					String text = content[i][j];
					contentStream.beginText();
					contentStream.moveTextPositionByAmount(textx,texty);
					contentStream.drawString(text);
					contentStream.endText();
					textx += colWidth;
				}
				texty-=rowHeight;
				textx = margin+cellMargin;
			}
			}
	void MjesecniSumarni(String fajl, int mjesec, String godina) throws Exception {
		DateFormat mj = new SimpleDateFormat("MM");
		DateFormat god = new SimpleDateFormat("yyyy");
		Date date = new Date();
		if(Integer.valueOf(god.format(date)).intValue() < Integer.valueOf(godina).intValue()) throw new Exception ("Pogrešna godina!");
		if(Integer.valueOf(god.format(date)).intValue() == Integer.valueOf(godina).intValue() && Integer.valueOf(mj.format(date)).intValue() < mjesec)
			throw new Exception ("Pogrešan mjesec!");
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();   
		int neizvrseni = 0; int izvrseni = 0; int stornirani = 0; 
		neizvrseni = Integer.parseInt(((session.createSQLQuery("select count(*) from radninalog where month(datumkreiranja) = "+ mjesec + " and year(datumkreiranja) = " + godina + " and status = 'nezakljucen'")).list().toArray()[0]).toString());
		izvrseni = Integer.parseInt(((session.createSQLQuery("select count(*) from radninalog where month(datumkreiranja) = "+ mjesec + " and year(datumkreiranja) = " + godina + " and status = 'zakljucen'")).list().toArray()[0]).toString());
		stornirani = Integer.parseInt(((session.createSQLQuery("select count(*) from radninalog where month(datumkreiranja) = "+ mjesec + " and year(datumkreiranja) = " + godina + " and status = 'storniran'")).list().toArray()[0]).toString());
		
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream(fajl));
	        document.open();
	        PdfPTable table = new PdfPTable(2);
	        
	        table.setWidthPercentage(50f);
	        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
	        	      Font.BOLD);
	        Font ctFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
	        	      Font.BOLD);
	        Paragraph parah = new Paragraph("Kreirani, zakljuceni, nezakljuceni i stornirani radni nalozi", catFont);
	        parah.setAlignment(Element.ALIGN_CENTER);
	        
	        document.add(parah);
	        parah = new Paragraph("Mjesec: " + mjesec + "  Godina: " + godina, ctFont);
	        parah.setAlignment(Element.ALIGN_CENTER);
	        document.add(parah);
	        document.add(new Paragraph(" "));document.add(new Paragraph(" "));
	        table.setWidths(new float[]{(float)0.7,(float) 0.3});
	        PdfPCell cell;
	        cell = new PdfPCell(new Phrase("Kreirani radni nalozi"));cell.setBackgroundColor(WebColors.getRGBColor("#d3d3d3"));table.addCell(cell);
	        cell = new PdfPCell(new Phrase(String.valueOf(neizvrseni + izvrseni + stornirani))); table.addCell(cell);
	        cell = new PdfPCell(new Phrase("Zakljuceni"));table.addCell(cell);
	        cell = new PdfPCell(new Phrase(String.valueOf(izvrseni)));table.addCell(cell);
	        cell = new PdfPCell(new Phrase("Nezakljuceni")); table.addCell(cell);
	        cell = new PdfPCell(new Phrase(String.valueOf(neizvrseni))); table.addCell(cell);
	        cell = new PdfPCell(new Phrase("Stornirani"));table.addCell(cell);
	        cell = new PdfPCell(new Phrase(String.valueOf(stornirani))); table.addCell(cell);
	        document.add(table);
	        document.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void MjesecniStornirani(String fajl, int mjesec, String godina) throws Exception {
		Document document = new Document();
		DateFormat mj = new SimpleDateFormat("MM");
		DateFormat god = new SimpleDateFormat("yyyy");
		Date date = new Date();
		if(Integer.valueOf(god.format(date)).intValue() < Integer.valueOf(godina).intValue()) throw new Exception ("Pogrešna godina!");
		if(Integer.valueOf(god.format(date)).intValue() == Integer.valueOf(godina).intValue() && Integer.valueOf(mj.format(date)).intValue() < mjesec)
			throw new Exception ("Pogrešan mjesec!");
        try {
        	Session session = HibernateUtil.getSessionFactory().openSession();
    		Transaction t = session.beginTransaction();     
    		List<Object[]> lq =session.createSQLQuery("select r.brojradnognaloga, r.datumkreiranja, r.posao, z.ime, z.prezime, r.razlogstorniranja from radninalog r, zaposlenik z where z.id = r.osobakojastornira and r.status = 'storniran' and month(r.datumkreiranja) = " +String.valueOf(mjesec)+ " and year(r.datumkreiranja) = " + godina ).list();
    		String[][] elementi = new String[lq.size()][6];
    		int i = 0; int j = 0;
    		PdfWriter.getInstance(document, new FileOutputStream(fajl));
	        document.open();
	        PdfPTable table = new PdfPTable(7);
	        
	        table.setWidthPercentage(95f);
	        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
	        	      Font.BOLD);
	        Font ctFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
	        	      Font.BOLD);
	        Paragraph parah = new Paragraph("Stornirani radni nalozi", catFont);
	        parah.setAlignment(Element.ALIGN_CENTER);
	        document.add(parah);
	        parah = new Paragraph("Mjesec: " + mjesec + "  Godina: " + godina, ctFont);
	        parah.setAlignment(Element.ALIGN_CENTER);
	        document.add(parah);
	        document.add(new Paragraph(" "));document.add(new Paragraph(" "));
	        table.setWidths(new float[]{(float)0.04,(float) 0.1,(float) 0.17,(float) 0.18,(float) 0.1, (float) 0.08,(float) 0.33});
	        PdfPCell cell;
	        cell = new PdfPCell(new Phrase("R.br."));cell.setHorizontalAlignment(Element.ALIGN_CENTER); cell.setBackgroundColor(WebColors.getRGBColor("#d3d3d3"));table.addCell(cell);
	        cell = new PdfPCell(new Phrase("Broj RN")); cell.setHorizontalAlignment(Element.ALIGN_CENTER);cell.setBackgroundColor(WebColors.getRGBColor("#d3d3d3"));table.addCell(cell);
	        cell = new PdfPCell(new Phrase("Datum kreiranja"));cell.setHorizontalAlignment(Element.ALIGN_CENTER);cell.setBackgroundColor(WebColors.getRGBColor("#d3d3d3")); table.addCell(cell);
	        cell = new PdfPCell(new Phrase("Posao"));cell.setHorizontalAlignment(Element.ALIGN_CENTER); cell.setBackgroundColor(WebColors.getRGBColor("#d3d3d3"));table.addCell(cell);
	        cell = new PdfPCell(new Phrase("Osoba koja stornira"));cell.setHorizontalAlignment(Element.ALIGN_CENTER);cell.setBackgroundColor(WebColors.getRGBColor("#d3d3d3")); table.addCell(cell);
	        cell = new PdfPCell(new Phrase("Stanje")); cell.setHorizontalAlignment(Element.ALIGN_CENTER);cell.setBackgroundColor(WebColors.getRGBColor("#d3d3d3"));table.addCell(cell);
	        cell = new PdfPCell(new Phrase("Razlog storniranja"));cell.setHorizontalAlignment(Element.ALIGN_CENTER);cell.setBackgroundColor(WebColors.getRGBColor("#d3d3d3")); table.addCell(cell);
    		for(Object[] q : lq) {
    			cell = new PdfPCell(new Phrase(String.valueOf(i + 1))); table.addCell(cell);
    			cell = new PdfPCell(new Phrase(String.valueOf(q[0])));table.addCell(cell);
    			cell = new PdfPCell(new Phrase(String.valueOf(q[1])));table.addCell(cell);
    			cell = new PdfPCell(new Phrase(String.valueOf(q[2])));table.addCell(cell);
    			cell = new PdfPCell(new Phrase( String.valueOf(q[3]) + " " + String.valueOf(q[4])));table.addCell(cell);
    			cell = new PdfPCell(new Phrase("Storniran"));table.addCell(cell);
    			cell = new PdfPCell(new Phrase(String.valueOf(q[5])));table.addCell(cell);
    			i++;
    		}
		
	        document.add(table);
	        document.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
		
	}
	void SedmicniRadnici(String fajl, Date datePicker) {
		Document document = new Document();
        try {
        	Session session = HibernateUtil.getSessionFactory().openSession();
    		Transaction t = session.beginTransaction();
    		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    		List<Object[]> lq =session.createSQLQuery("select z.id, z.ime, z.prezime, "
    				+ "(select count(rn.brojradnognaloga) from zaposlenik zn, radninalog rn where rn.kreatorradnognaloga = z.id and "
    				+ "rn.status = 'zakljucen' and z.id = zn.id  and "
    				+ "week(rn.datumkreiranja) = week('"+ dateFormat.format(datePicker) + "')), "
    				+ "(select count(rn.brojradnognaloga) from zaposlenik zn, radninalog rn where rn.kreatorradnognaloga = z.id and "
    				+ "rn.status = 'nezakljucen' and z.id = zn.id  and "
    				+ "week(rn.datumkreiranja) = week('"+ dateFormat.format(datePicker) + "')), (select count(rn.brojradnognaloga) from zaposlenik zn, radninalog rn"
    				+ " where rn.kreatorradnognaloga = z.id and rn.status = 'storniran' and z.id = zn.id and "
    				+ "week(rn.datumkreiranja) = week('"+ dateFormat.format(datePicker) + "'))"
    				+ " from zaposlenik z, radninalog r where r.izvrsilacposla = z.id group by z.id").list();
    		
    		int i = 0; int j = 0;
    		PdfWriter.getInstance(document, new FileOutputStream(fajl));
	        document.open();
	        PdfPTable table = new PdfPTable(5);
	        
	        table.setWidthPercentage(95f);
	        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
	        	      Font.BOLD);
	        Font ctFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
	        	      Font.BOLD);
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(datePicker);
	        int week = cal.get(Calendar.WEEK_OF_YEAR);
	        int year = cal.get(Calendar.YEAR);
	        Paragraph parah = new Paragraph("Sedmicni izvjestaj o radu zaposlenika", catFont);
	        parah.setAlignment(Element.ALIGN_CENTER);
	        document.add(parah);
	        parah = new Paragraph("Sedmica: " + week + ".  Godina: " + year, ctFont);
	        parah.setAlignment(Element.ALIGN_CENTER);
	        document.add(parah);
	        document.add(new Paragraph(" "));document.add(new Paragraph(" "));
	        table.setWidths(new float[]{(float) 0.2,(float) 0.2,(float) 0.2,(float) 0.2, (float) 0.2});
	        PdfPCell cell;
	        cell = new PdfPCell(new Phrase("Id"));cell.setHorizontalAlignment(Element.ALIGN_CENTER); cell.setBackgroundColor(WebColors.getRGBColor("#d3d3d3"));table.addCell(cell);
	        cell = new PdfPCell(new Phrase("Ime i prezime")); cell.setHorizontalAlignment(Element.ALIGN_CENTER);cell.setBackgroundColor(WebColors.getRGBColor("#d3d3d3"));table.addCell(cell);
	        cell = new PdfPCell(new Phrase("Broj zakljucenih"));cell.setHorizontalAlignment(Element.ALIGN_CENTER);cell.setBackgroundColor(WebColors.getRGBColor("#d3d3d3")); table.addCell(cell);
	        cell = new PdfPCell(new Phrase("Broj nezakljucenih"));cell.setHorizontalAlignment(Element.ALIGN_CENTER); cell.setBackgroundColor(WebColors.getRGBColor("#d3d3d3"));table.addCell(cell);
	        cell = new PdfPCell(new Phrase("Broj storniranih"));cell.setHorizontalAlignment(Element.ALIGN_CENTER);cell.setBackgroundColor(WebColors.getRGBColor("#d3d3d3")); table.addCell(cell);
	        
    		for(Object[] q : lq) {
    			cell = new PdfPCell(new Phrase(String.valueOf(q[0])));cell.setHorizontalAlignment(Element.ALIGN_CENTER); table.addCell(cell);
    			cell = new PdfPCell(new Phrase(String.valueOf(q[1] + " " + q[2])));cell.setHorizontalAlignment(Element.ALIGN_CENTER);table.addCell(cell);
    			cell = new PdfPCell(new Phrase(String.valueOf(q[3])));cell.setHorizontalAlignment(Element.ALIGN_CENTER);table.addCell(cell);
    			cell = new PdfPCell(new Phrase(String.valueOf(q[4])));cell.setHorizontalAlignment(Element.ALIGN_CENTER);table.addCell(cell);
    			cell = new PdfPCell(new Phrase( String.valueOf(q[5])));cell.setHorizontalAlignment(Element.ALIGN_CENTER);table.addCell(cell);
    			i++;
    		}
		
	        document.add(table);
	        document.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
		
	}
	
}

