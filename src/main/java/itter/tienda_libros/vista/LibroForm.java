package itter.tienda_libros.vista;

import itter.tienda_libros.modelo.Libro;
import itter.tienda_libros.servicio.LibroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
public class LibroForm extends JFrame {
    LibroServicio libroServicio;
    private JPanel panel;
    private JTable tablaLibros;
    private JTextField idTexto;
    private JTextField libroTexto;
    private JTextField autorTexto;
    private JTextField precioTexto;
    private JTextField stockTexto;
    private JButton agregarButton;
    private JButton modificarButton;
    private JButton eliminarButton;
    private DefaultTableModel tablaModeloLibros;

    //Inyectamos la dependencia de Spring a traves del constructor
    @Autowired
    public LibroForm(LibroServicio libroServicio){
        this.libroServicio = libroServicio;

        iniciarForma();

        agregarButton.addActionListener(e -> agregarLibro());

        tablaLibros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cargarLibroSeleccionado();
                //Cuando se selecciona algun libro de la lista se desactiva el boton agregar para que no se creen copias
                agregarButton.setEnabled(false);

            }
        });

        modificarButton.addActionListener(e -> modificarLibro());

        eliminarButton.addActionListener(e ->  eliminarLibro());
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

    private void agregarLibro(){
        //Leer los valores del formulario
        if(libroTexto.getText().equals("")){
            mostrarMensaje("Debe proporcionar el nombre del Libro");
            //Movemos el cursor al cuadro de nombre de libro
            libroTexto.requestFocusInWindow();
            return;
        }
        //Recuperamos variables del formulario
        var nombreLibro = libroTexto.getText();
        var autorLibro = autorTexto.getText();
        var precioLibro = Double.parseDouble(precioTexto.getText());
        var stockLibro = Integer.parseInt(stockTexto.getText());
        //Creamos el objeto LIbro
        var libro = new Libro(null,nombreLibro,autorLibro,precioLibro,stockLibro);
        this.libroServicio.guardarLibro(libro);
        mostrarMensaje("Se agrego el Libro " +nombreLibro);
        limpiarFormulario();
        listaLibros();
    }

    private void cargarLibroSeleccionado(){
        var filaSeleccionada = tablaLibros.getSelectedRow();
        if(filaSeleccionada != -1){
            String idLibro = tablaLibros.getModel().getValueAt(filaSeleccionada,0).toString();
            idTexto.setText(idLibro);
            var libro = libroServicio.buscarLibroPorId(Integer.parseInt(idLibro));
            libroTexto.setText(libro.getNombreLibro());
            autorTexto.setText(libro.getAutor());
            precioTexto.setText(String.valueOf(libro.getPrecio()));
            stockTexto.setText(String.valueOf(libro.getStock()));
        }

    }

    private void modificarLibro(){
        if (this.idTexto.getText().equals("")){
            mostrarMensaje("Debe seleccionar un libro de la lista...");
            return;
        } else {
            if(libroTexto.getText().equals("")){
                mostrarMensaje("El nombre del libro no puede estar vacio");
                //Movemos el cursor al cuadro de nombre de libro
                libroTexto.requestFocusInWindow();
                return;
            }

            Integer idLibro = Integer.parseInt(idTexto.getText());
            var nombreLibro = libroTexto.getText();
            var autorLibro = autorTexto.getText();
            var precioLibro = Double.parseDouble(precioTexto.getText());
            var stockLibro = Integer.parseInt(stockTexto.getText());
            //Creamos el objeto LIbro
            var libro = new Libro(idLibro,nombreLibro,autorLibro,precioLibro,stockLibro);
            this.libroServicio.guardarLibro(libro);
            mostrarMensaje("Se modificó el libro...");
            limpiarFormulario();
            listaLibros();
            agregarButton.setEnabled(true);
        }

    }

    private void eliminarLibro(){
        if (this.idTexto.getText().equals("")){
            mostrarMensaje("Debe seleccionar un libro de la lista...");
            return;
        } else {
            Integer idLibro = Integer.parseInt(idTexto.getText());
            var nombreLibro = libroTexto.getText();
            var autorLibro = autorTexto.getText();
            var precioLibro = Double.parseDouble(precioTexto.getText());
            var stockLibro = Integer.parseInt(stockTexto.getText());
            //Creamos el objeto LIbro
            var libro = new Libro(idLibro,nombreLibro,autorLibro,precioLibro,stockLibro);
            this.libroServicio.eliminarLibro(libro);
            mostrarMensaje("Se eliminó el libro...");
            limpiarFormulario();
            listaLibros();
            agregarButton.setEnabled(true);
        }
    }

    private void limpiarFormulario(){
        libroTexto.setText("");
        autorTexto.setText("");
        precioTexto.setText("");
        stockTexto.setText("");
    }

    private void mostrarMensaje(String mensaje){
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        //Creamos un elemento de idTexto oculto
        idTexto = new JTextField("");
        idTexto.setVisible(false);

        this.tablaModeloLibros = new DefaultTableModel(0,5){
            @Override
            public boolean isCellEditable(int row, int column){return false;}
        };

        String[] cabeceras = {"Id", "Libro", "Autor", "Precio", "Stock"};
        this.tablaModeloLibros.setColumnIdentifiers(cabeceras);
        //Instanciamos el Jtable
        this.tablaLibros = new JTable(tablaModeloLibros);
        //Evitamos que se seleccionen varios registros
        tablaLibros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
