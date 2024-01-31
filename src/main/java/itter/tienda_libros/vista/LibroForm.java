package itter.tienda_libros.vista;

import itter.tienda_libros.servicio.LibroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

@Component
public class LibroForm extends JFrame {
    LibroServicio libroServicio;
    private JPanel panel;
    private JTable tablaLibros;
    private JTextField libroTexto;
    private JTextField autorTexto;
    private JTextField precioTexto;
    private JTextField sotckTexto;
    private JButton agregarButton;
    private JButton modificarButton;
    private JButton eliminarButton;
    private DefaultTableModel tablaModeloLibros;

    //Inyectamos la dependencia de Spring a traves del constructor
    @Autowired
    public LibroForm(LibroServicio libroServicio){
        this.libroServicio = libroServicio;
        iniciarForma();
        agregarButton.addActionListener(e -> {

        });
    }

    //Metodo para mostrar la ventana y recuperar la informacion de la base de datos
    private void iniciarForma(){
        //
        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(900,700);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension tamanioPantalla = toolkit.getScreenSize();
        int x = (tamanioPantalla.width - getWidth())/2;
        int y = (tamanioPantalla.height - getHeight())/2;
        setLocation(x,y);

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        this.tablaModeloLibros = new DefaultTableModel(0,5);
        String[] cabeceras = {"Id", "Libro", "Autor", "Precio", "Stock"};
        this.tablaModeloLibros.setColumnIdentifiers(cabeceras);
        //Instanciamos el Jtable
        this.tablaLibros = new JTable(tablaModeloLibros);
        listaLibros();
    }

    private void listaLibros(){
        //Limpiar la tabla
        tablaModeloLibros.setRowCount(0);
        //Obtener los libros
        var libros = libroServicio.listarLibros();
        libros.forEach((libro)->{
            Object[] renglonLibro ={
                    libro.getIdLibro(),
                    libro.getNombreLibro(),
                    libro.getAutor(),
                    libro.getPrecio(),
                    libro.getStock()
            };
            this.tablaModeloLibros.addRow(renglonLibro);
        });
    }
}
