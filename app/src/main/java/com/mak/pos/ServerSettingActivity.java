package com.mak.pos;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.mak.Api.ApiClient;
import com.mak.Api.ApiInterface;
import com.mak.App;
import com.mak.Constant;
import com.mak.pos.Model.POJO.Cart;
import com.mak.pos.Model.POJO.Items;
import com.mak.pos.Model.POJO.RestaurantDetails;
import com.mak.pos.Model.POJO.UserModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Callback;
import retrofit2.Response;

public class ServerSettingActivity extends AppCompatActivity {

    EditText input_url,input_port;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_server);

        input_url=(EditText)findViewById(R.id.input_url);
        input_port=(EditText)findViewById(R.id.input_port);


        Cart cart=new Cart();
        cart.setRoundoff("0.50");
        cart.setSrl("123456");
        cart.setTableCode("111");
        cart.setBillno("BILLLL");
        cart.setDocdate("11-1-19");
        cart.setDoctime("11-1212121-19");
        cart.setPaxNo("8");
        cart.setNoofPrints("1");
        cart.setTotalbillAmount(500);
        cart.setTotalDiscAmt("50");
        Items items=new Items();
        items.setQty(2);
        items.setAddtaxAmt(5);
        items.setAddtaxAmt2(5);
        items.setDiscAmt(2);
        items.setItem_name("JOLLY FOOD");
        items.setRate(150);
        items.setTaxamt(10);
        ArrayList<Items> items1=new ArrayList<>();
        for(int i=0;i<5;i++) {
            items1.add(items);
        }
        cart.setItems(items1);
        RestaurantDetails details=new RestaurantDetails();
        details.setRAddress("abad");
        details.setRGSTNo("32231");
        details.setRName("AAAA");
        details.setRTelephone("1234444");
        cart.setRestaurantDetails(details);
        createPdf(cart);
        findViewById(R.id.btn_Done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(input_url.getText().toString().trim().length()==0||input_url.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Please enter url",Toast.LENGTH_LONG).show();

                }else if(!input_url.getText().toString().trim().contains("http://"))
                {
                    Toast.makeText(getApplicationContext(),"Url start with http://",Toast.LENGTH_LONG).show();

                }
                        else if(input_port.getText().toString().trim().length()==0||input_port.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Please enter port",Toast.LENGTH_LONG).show();
                }
                else
                {
                    App.getPrefs().setString("URL",input_url.getText().toString().trim());
                    App.getPrefs().setString("PORT",input_port.getText().toString().trim());

                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    finish();
                }


            }
        });
    }

    protected void createPdf(Cart cart) {


        String imageFileName = "POS_Invoice_" + cart.getSrl();


        String folderPath = Environment.getExternalStorageDirectory() + "/POS/Invoice";
        File folder = new File(folderPath);
        if (!folder.exists()) {
            File wallpaperDirectory = new File(folderPath);
            wallpaperDirectory.mkdirs();
        }
        //create a new file
        File pdfFile = new File(folderPath, imageFileName + ".pdf");

        if (pdfFile.exists()) {
            pdfFile.delete();
        }

        try {
            /**
             * Creating Document
             */
            Document document = new Document();

            // Location to save
            PdfWriter.getInstance(document, new FileOutputStream(pdfFile.getAbsolutePath()));

            // Open to write
            document.open();

            // Document Settings
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor(getResources().getString(R.string.app_name));
            document.addCreator(getResources().getString(R.string.app_name));

            /***
             * Variables for further use....
             */
            BaseColor mColorAccent = new BaseColor(0, 153, 204, 255);
            float mHeadingFontSize = 20.0f;
            float mValueFontSize = 26.0f;

            /**
             * How to USE FONT....
             */
            BaseFont urName = BaseFont.createFont("assets/arial.ttf", "UTF-8", BaseFont.EMBEDDED);

            // LINE SEPARATOR
            LineSeparator lineSeparator = new LineSeparator();
            lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));
            lineSeparator.setPercentage(80);
            // Title Order Details...
            // Adding Title....


            Font mInvoiceFont = new Font(urName, 28.0f, Font.BOLD, BaseColor.BLACK);
            Chunk mInvoiceChunk = new Chunk("RETAIL INVOICE", mInvoiceFont);
            Paragraph mInvoiceParagraph = new Paragraph(mInvoiceChunk);
            mInvoiceParagraph.setAlignment(Element.ALIGN_CENTER);
            Paragraph mBillph = new Paragraph();

            if(cart.getBillno()!=null) {
                Font mBillNumberFont = new Font(urName, 16.0f, Font.NORMAL, BaseColor.BLACK);

                Chunk mBillChunk = new Chunk("BILL NUMBER. :" + cart.getBillno(), mBillNumberFont);
                mBillph=new Paragraph(mBillChunk);
                mBillph.setAlignment(Element.ALIGN_LEFT);
            }


            Paragraph mOrderDetailsTitleParagraph=new Paragraph();
            Paragraph resInfoParagraph=new Paragraph();
            Font mOrderDetailsTitleFont = new Font(urName, 25.0f, Font.BOLD, BaseColor.BLACK);




            if(cart!=null&&cart.getRestaurantDetails()!=null) {
                String resName = "";

                if(cart.getRestaurantDetails().getRName()!=null)
                    resName=cart.getRestaurantDetails().getRName();

                Chunk mOrderDetailsTitleChunk = new Chunk(resName, mOrderDetailsTitleFont);
                mOrderDetailsTitleParagraph = new Paragraph(mOrderDetailsTitleChunk);
                mOrderDetailsTitleParagraph.setAlignment(Element.ALIGN_CENTER);


                Font resInfo = new Font(urName, 16.0f, Font.NORMAL, BaseColor.BLACK);
                String resAddress = "";
                if(cart.getRestaurantDetails().getRAddress()!=null)
                    resAddress=cart.getRestaurantDetails().getRAddress();
                String resGST = "";
                if(cart.getRestaurantDetails().getRGSTNo()!=null)
                    resGST=cart.getRestaurantDetails().getRGSTNo();

                String resEmail = "";
                String resNumber = "";

                if(cart.getRestaurantDetails().getRTelephone()!=null)
                    resNumber="Tel:Ph :"+cart.getRestaurantDetails().getRTelephone();

                //String resInvoiceNumber = "#Invoice No: " + cart.getSrl();
                String billDate = "Date : " + com.mak.Constant.getBillDateTime(new Date());

                String allResInfo = resAddress + " \n" + resGST + " \n" + resEmail + " \n" + resNumber + " \n" ;
                Chunk resInfoChunk = new Chunk(allResInfo, resInfo);
                resInfoParagraph = new Paragraph(resInfoChunk);
                resInfoParagraph.setAlignment(Element.ALIGN_CENTER);
            }



            PdfPTable billTable = new PdfPTable(6); //one page contains 15 records
            billTable.setWidthPercentage(100);

            billTable.setWidths(new float[] { 2, 5,2,2,2,2 });
            billTable.setSpacingBefore(30.0f);


            //BILL INFO
            PdfPTable billInfo = new PdfPTable(4);
            billInfo.setWidthPercentage(100);
            billInfo.addCell(getAccountsCell("TABLE :"));
            billInfo.addCell(getTableInfoCellR(cart.getTableCode()));
            billInfo.addCell(getAccountsCell("DATE:"));
            billInfo.addCell(getTableInfoCellR(cart.getDocdate()));


            PdfPCell tableInfo = new PdfPCell (billInfo);
            tableInfo.setBorder(Rectangle.NO_BORDER);
            tableInfo.setColspan (4);
            billTable.addCell(tableInfo);


            PdfPTable validity1 = new PdfPTable(2);
            validity1.setWidthPercentage(100);
            validity1.addCell(getValidityCell(" "));
           ;
            PdfPCell summaryL1 = new PdfPCell (validity1);
            summaryL1.setBorder(Rectangle.NO_BORDER);

            summaryL1.setColspan (2);
            summaryL1.setPadding (1.0f);
            billTable.addCell(summaryL1);




            //bill 2

            //BILL INFO
            PdfPTable billInfo1 = new PdfPTable(6);
            billInfo1.setWidthPercentage(100);

            billInfo1.addCell(getAccountsCell("TIME :"));
            billInfo1.addCell(getTableInfoCellR(cart.getDoctime()));
            billInfo1.addCell(getAccountsCell(""));
            billInfo1.addCell(getTableInfoCellR(cart.getNoofPrints()));
            billInfo1.addCell(getAccountsCell("PAPX :"));
            billInfo1.addCell(getTableInfoCellR(cart.getPaxNo()));



            PdfPCell tableInfo1 = new PdfPCell (billInfo1);
            tableInfo1.setBorder(Rectangle.NO_BORDER);
            tableInfo1.setColspan (6);
            billTable.addCell(tableInfo1);


           /* PdfPTable validity2 = new PdfPTable(2);
            validity2.setWidthPercentage(100);
            validity2.addCell(getValidityCell(" "));
            ;
            PdfPCell summaryL2 = new PdfPCell (validity2);
            summaryL1.setBorder(Rectangle.NO_BORDER);

            summaryL2.setColspan (2);
            summaryL2.setPadding (1.0f);
            billTable.addCell(summaryL2);
*/




            billTable.addCell(getBillHeaderCell("No"));
            billTable.addCell(getBillHeaderCell("Name"));
            billTable.addCell(getBillHeaderCell("QTY"));
            billTable.addCell(getBillHeaderCell("CGST"));
            billTable.addCell(getBillHeaderCell("SGST"));
            billTable.addCell(getBillHeaderCell("Amount"));
            float total = 0, discAmount = 0, taxAmount = 0, addTax = 0, surCharge = 0;
            if(cart!=null&&cart.getItems()!=null&&cart.getItems().size()>0)
            {
                for(int i=0;i<cart.getItems().size();i++)
                {
                    Items items = cart.getItems().get(i);


                    if(items!=null)
                    {
                        int no=i+1;

                        billTable.addCell(getBillRowCell(String.valueOf(no)));
                        billTable.addCell(getBillRowCell(getValue(items.getItem_name())));
                        billTable.addCell(getBillRowCell(getValue(String.valueOf(items.getQty()))));
                        billTable.addCell(getBillRowCell(getValue(String.valueOf(items.getTaxamt()))));
                        billTable.addCell(getBillRowCell(getValue(String.valueOf(items.getAddtaxAmt()))));
                        billTable.addCell(getBillRowCell(getValue(String.valueOf(items.getRate()))));

                        if (items.getQty() > 0) {
                            if (items != null && items.getDiscAmt() != -1)
                                discAmount = discAmount + items.getDiscAmt();
                            if (items != null && items.getTaxamt() != -1)
                                taxAmount = taxAmount + items.getTaxamt();

                            if (items != null && items.getAddtaxAmt() != -1)
                                addTax = addTax + items.getAddtaxAmt();

                            if (items != null && items.getAddtaxAmt2() != -1)
                                surCharge = surCharge + items.getAddtaxAmt2();

                            if (items != null && items.getRate() != -1)
                                total = total + (items.getRate() * items.getQty());
                        }

                    }

                }
                for(int i=0;i<2;i++)
                {
                    billTable.addCell(getBillRowCell(" "));
                    billTable.addCell(getBillRowCell(""));
                    billTable.addCell(getBillRowCell(""));
                    billTable.addCell(getBillRowCell(""));
                    billTable.addCell(getBillRowCell(""));
                    billTable.addCell(getBillRowCell(""));
                }
            }

            PdfPTable validity = new PdfPTable(1);
            validity.setWidthPercentage(100);
            validity.addCell(getValidityCell(" "));
            validity.addCell(getValidityCell(""));
            validity.addCell(getValidityCell(""));
            validity.addCell(getValidityCell(""));
            validity.addCell(getValidityCell(""));
            validity.addCell(getValidityCell(""));
            validity.addCell(getValidityCell(""));
            PdfPCell summaryL = new PdfPCell (validity);
            summaryL.setBorder(Rectangle.NO_BORDER);

            summaryL.setColspan (2);
            summaryL.setPadding (1.0f);
            billTable.addCell(summaryL);

            PdfPTable accounts = new PdfPTable(2);
            accounts.setWidthPercentage(100);
            accounts.addCell(getAccountsCell("NET"));
            accounts.addCell(getAccountsCellR(com.mak.pos.Utility.Constant.twoDigitValue(total)));
            accounts.addCell(getAccountsCell("SGST"));
            accounts.addCell(getAccountsCellR("+"+ com.mak.pos.Utility.Constant.twoDigitValue(addTax)));
            accounts.addCell(getAccountsCell("CGST"));
            accounts.addCell(getAccountsCellR("+"+ com.mak.pos.Utility.Constant.twoDigitValue(taxAmount)));
            accounts.addCell(getAccountsCell("SURCHARGE"));
            accounts.addCell(getAccountsCellR("+"+ com.mak.pos.Utility.Constant.twoDigitValue(surCharge)));
            accounts.addCell(getAccountsCell("DISCOUNT"));
            if(cart.getSpecialDiscount()!=null)
                accounts.addCell(getAccountsCellR("-"+ com.mak.pos.Utility.Constant.twoDigitValue(total-cart.getTotalbillAmount())));
            else
                accounts.addCell(getAccountsCellR("-"+ com.mak.pos.Utility.Constant.twoDigitValue(discAmount)));

            accounts.addCell(getAccountsCell("ROUND OFF"));
            if (cart != null && cart.getRoundoff() != null && !cart.getRoundoff().equals("")) {
                accounts.addCell(getAccountsCellR("-"+ com.mak.pos.Utility.Constant.twoDigitValue(Float.parseFloat(cart.getRoundoff()))));
            } else {
                accounts.addCell(getAccountsCellR("-0.00"));
            }


            accounts.addCell(getAccountsCellGrandTotal("BILL AMOUNT"));
            accounts.addCell(getAccountsCellRGrandTotal(com.mak.pos.Utility.Constant.twoDigitValue(cart.getTotalbillAmount())));
            PdfPCell summaryR = new PdfPCell (accounts);
            summaryR.setBorder(Rectangle.NO_BORDER);
            summaryR.setColspan (4);
            billTable.addCell(summaryR);










            //tabel info

           /* PdfPTable tabInfo1 = new PdfPTable(1);
            validity.setWidthPercentage(100);
            validity.addCell(getValidityCell(" "));
            validity.addCell(getValidityCell(""));
            validity.addCell(getValidityCell(""));
            validity.addCell(getValidityCell(""));
            validity.addCell(getValidityCell(""));
            validity.addCell(getValidityCell(""));
            validity.addCell(getValidityCell(""));
            PdfPCell summaryL = new PdfPCell (validity);
            summaryL.setBorder(Rectangle.NO_BORDER);

            summaryL.setColspan (2);
            summaryL.setPadding (1.0f);
            billTable.addCell(summaryL);*/

            PdfPTable tabInfo = new PdfPTable(2);
            tabInfo.setWidthPercentage(100);
            tabInfo.addCell(getAccountsCell("NET"));
            tabInfo.addCell(getAccountsCellR(com.mak.pos.Utility.Constant.twoDigitValue(total)));
            tabInfo.addCell(getAccountsCell("SGST"));
            tabInfo.addCell(getAccountsCellR("+"+ com.mak.pos.Utility.Constant.twoDigitValue(addTax)));

            PdfPCell summaryInfo = new PdfPCell (tabInfo);
            summaryInfo.setBorder(Rectangle.NO_BORDER);
            summaryInfo.setColspan (4);
            billTable.addCell(summaryInfo);






/*
            //BILL INFO
            PdfPTable billInfo1= new PdfPTable(2);
            billInfo1.setWidthPercentage(100);
            billInfo1.addCell(getAccountsCell("TIME :"));
            billInfo1.addCell(getAccountsCellR(cart.getDoctime()));
            billInfo1.addCell(getAccountsCell(""));
            billInfo1.addCell(getAccountsCellR(cart.getNoofPrints()));
            billInfo1.addCell(getAccountsCell("PAPX :"));
            billInfo1.addCell(getAccountsCellR(cart.getPaxNo()));


            PdfPCell tableInfo1 = new PdfPCell (billInfo1);
            tableInfo.setBorder(Rectangle.NO_BORDER);
            tableInfo.setColspan (3);
            billTable.addCell(tableInfo1);*/



            PdfPTable ServiceCode = new PdfPTable(1);
            ServiceCode.setWidthPercentage(100);
            PdfPTable ServiceCode1 = new PdfPTable(1);
            ServiceCode1.setWidthPercentage(100);

            ServiceCode.addCell(getdescCellRight("Service Code: "+cart.getStoreCode()));
            ServiceCode1.addCell(getdescCellLeft(""+cart.getCaptain()));


            PdfPTable describer = new PdfPTable(1);
            describer.setWidthPercentage(100);
            describer.addCell(getdescCell(" "));
            describer.addCell(getdescCell(" "));
            describer.addCell(getdescCell("No Reverse Charges"));

            document.open();//PDF document opened........


            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));
            document.add(mOrderDetailsTitleParagraph);
            document.add(resInfoParagraph);
            document.add(mBillph);

            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));

            document.add(billTable);
            document.add(ServiceCode);
            document.add(ServiceCode1);
            document.add(describer);

            document.close();



            document.close();

            if (pdfFile != null && pdfFile.exists()) {


                try {
                    MediaScannerConnection.scanFile(ServerSettingActivity.this,
                            new String[]{pdfFile.toString()}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String path, Uri uri) {

                                }
                            });
                } catch (Exception e) {
                }
            }


            //   FileUtils.openFile(mContext, new File(dest));





        } catch (IOException | DocumentException ie) {
            Log.e("createPdf: Error ", " ");
        } catch (ActivityNotFoundException ae) {
            Toast.makeText(ServerSettingActivity.this, "Something wrong .", Toast.LENGTH_SHORT).show();
        }
    }

    private String getValue(String itemVaue) {
        if(itemVaue==null)
            return " ";
        else
            return  itemVaue;
    }

    public static PdfPCell getBillHeaderCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 15);
        font.setColor(BaseColor.BLACK);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setPadding (5.0f);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBackgroundColor (BaseColor.GRAY);
        return cell;
    }
    public static PdfPCell getBillRowCell(String text) {
        PdfPCell cell = new PdfPCell (new Paragraph (text));
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setPadding (5.0f);
        cell.setBorderWidthBottom(0);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBorderWidthTop(0);
        return cell;
    }

    public static PdfPCell getAccountsCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 15);

        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthTop(0);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding (5.0f);
        return cell;
    }
    public static PdfPCell getAccountsCellGrandTotal(String text) {
        FontSelector fs = new FontSelector();

        Font font = FontFactory.getFont(FontFactory.HELVETICA, 18);
        font.setStyle(Font.BOLD);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthTop(0);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding (5.0f);
        return cell;
    }
    public static PdfPCell getAccountsCellR(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 16);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthTop(0);
        cell.setHorizontalAlignment (Element.ALIGN_RIGHT);
        cell.setPadding (5.0f);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingRight(20.0f);
        return cell;
    }
    public static PdfPCell getTableInfoCellR(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 16);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthTop(0);
        cell.setHorizontalAlignment (Element.ALIGN_LEFT);
        cell.setPadding (5.0f);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingRight(20.0f);
        return cell;
    }
    public static PdfPCell getAccountsCellRGrandTotal(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 18);
        font.setStyle(Font.BOLD);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthTop(0);
        cell.setHorizontalAlignment (Element.ALIGN_RIGHT);
        cell.setPadding (5.0f);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingRight(20.0f);
        return cell;
    }

    public static PdfPCell getdescCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 22);
        font.setColor(BaseColor.BLACK);
        font.setStyle(Font.BOLD);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);

        cell.setBorder(0);
        return cell;
    }
    public static PdfPCell getdescCellRight(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 22);
        font.setColor(BaseColor.BLACK);
        font.setStyle(Font.BOLD);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setHorizontalAlignment (Element.ALIGN_RIGHT);
        cell.setBorder(Rectangle.NO_BORDER);

        cell.setBorder(0);
        return cell;
    } public static PdfPCell getdescCellLeft(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 22);
        font.setColor(BaseColor.BLACK);
        font.setStyle(Font.BOLD);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setHorizontalAlignment (Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);

        cell.setBorder(0);
        return cell;
    }
    public static PdfPCell getValidityCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        font.setColor(BaseColor.GRAY);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBorder(0);
        return cell;
    }

}
