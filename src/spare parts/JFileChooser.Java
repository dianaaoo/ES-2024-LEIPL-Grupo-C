public class JFileChooser {

    // I so wish this was Python

    // all the functions needed for the GUI to open a file browser window for opening // saving a file via JFileChooser

    // ! - actual opening // reading // writing // saving is done elsewhere, this is exclusively for the GUI code

    // chamar funções para abrir file browser para ABRIR um ficheiro
    JFileChooser fileBrowserWindow = new JFileChooser();

    fileBrowserWindow.setCurrentDirectory(new File("dir")); // isto muda o diretório onde o file browser abre
                                                            // trocar o dir pelo diretório desejado

    fileBrowserWindow.setCurrentDirectory(new File("."));   // esta versão abre o file browser no diretório da aplicação

    fileBrowserWindow.showOpenDialog(null);                 // isto abre a janela do file browser
                                                            // ! - o 'null' deve ser trocado pela janela parente, caso não haja, deixa-se

    int response = fileBrowserWindow.showOpenDialog(null);  // usado em altenrativa à linha anterior, para usar a linha seguinte
                                                            // não sei pq é que é int em vez de boolean

    if(response == JFileChooser.APPROVE_OPTION) {           // se o user escolher um ficheiro faz o if
                                                            // se o user cancelar ou fechar o file browser salta o if

        File file = new File(fileBrowserWindow.getSelectedFile().getAbsolutePath();)    // abre o ficheiro selecionado pelo user
                                                            // como ele vai buscar o absolute path, podemos usar isto para passar o
                                                            // path como argumento, ou podemos passar logo o ficheiro
                                                            // pessoalmente voto pelo ficheiro

        System.out.println(file);                           // (para aquele bom debug martelado)
    }



    // chamar funções para abrir file browser para GUARDAR um ficheiro
    JFileChooser fileBrowserWindow = new JFileChooser();

    fileBrowserWindow.setCurrentDirectory(new File("dir")); // isto muda o diretório onde o file browser abre
                                                            // trocar o dir pelo diretório desejado

    fileBrowserWindow.setCurrentDirectory(new File("."));   // esta versão abre o file browser no diretório da aplicação

    fileBrowserWindow.showSaveDialog(null);                 // isto abre a janela do file browser
                                                            // ! - o 'null' deve ser trocado pela janela parente, caso não haja, deixa-se

    int response = fileBrowserWindow.showSaveDialog(null);  // usado em altenrativa à linha anterior, para usar a linha seguinte
                                                            // não sei pq é que é int em vez de boolean

    if(response == JFileChooser.APPROVE_OPTION) {           // se o user escolher um ficheiro faz o if
                                                            // se o user cancelar ou fechar o file browser salta o if

        File file = new File(fileBrowserWindow.getSelectedFile().getAbsolutePath();)    // abre o ficheiro selecionado pelo user
                                                            // como ele vai buscar o absolute path, podemos usar isto para passar o
                                                            // path como argumento, o que aqui já faz mais sentido
                                                            
        System.out.println(file);                           // (para aquele bom debug martelado)
    }


}