/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package printReport;

import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarException;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class ReportManager {

    private static ReportManager instance;
    
    private JasperReport reportPay;
    

    public static ReportManager getInstance() {
        if (instance == null) {
            instance = new ReportManager();
        }
        return instance;
    }
    
    private ReportManager() {
        
    }
    
    public void compileReport() throws JRException {
        reportPay = JasperCompileManager.compileReport(getClass().getResourceAsStream("/PrintReport/reportBill.jrxml"));  
    }
    
    public void printReportPayment(ParameterReport data) throws JRException {
        Map para = new HashMap();
        para.put("tongTien", data.getTongTien());
        para.put("chietKhau", data.getChietKhau());
        para.put("phaiTra", data.getKhachTra());
        para.put("ngay", data.getNgay());
        para.put("ghiChu", data.getGhiChu());
        para.put("khuyenMai", data.getKhuyenMai());
        DefaultJasperReportsContext.getInstance();
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data.getListFields());
        JasperPrint print = JasperFillManager.fillReport(reportPay, para, dataSource);
        view(print);
    }
    
    private void view(JasperPrint print) throws JRException {
        JasperViewer.viewReport(print, false);
    }
}
