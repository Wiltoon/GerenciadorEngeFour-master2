package gerenciador.engefourjunior.com.gerenciadorengefour;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;


import java.util.List;

import gerenciador.engefourjunior.com.gerenciadorengefour.Model.VendaModel;
import gerenciador.engefourjunior.com.gerenciadorengefour.Repository.ClienteRepository;
import gerenciador.engefourjunior.com.gerenciadorengefour.Repository.ProdutoRepository;
import gerenciador.engefourjunior.com.gerenciadorengefour.Repository.VendaRepository;
import gerenciador.engefourjunior.com.gerenciadorengefour.Uteis.Alerta;

public class CadastrarVendaActivity extends AppCompatActivity {


    /*COMPONENTES DA TELA*/
    Spinner          spinnerCliente;
    Spinner          spinnerProduto;
    EditText         editTextQuantidade;
    TextView         textViewValorTotal;
    TextView          textViewValorDevendo;
    EditText         editTextValorPago;
    Button           buttonSalvar;
    Button           buttonVoltar;
    ProdutoRepository produtoRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_venda);

        //VINCULA OS COMPONENTES DA TELA COM OS DA ATIVIDADE
        this.CriarComponentes();

        //CRIA OS EVENTOS DOS COMPONENTES
        this.CriarEventos();

        //POPULA OS SPINNERS DA ACTIVITY
        this.popularSpinner();
    }

    //VINCULA OS COMPONENTES DA TELA COM OS DA ATIVIDADE
    protected  void CriarComponentes(){

        spinnerCliente         =(Spinner) this.findViewById(R.id.spinnerCliente);

        spinnerProduto         =(Spinner) this.findViewById(R.id.spinnerProduto);

        editTextQuantidade     =(EditText) this.findViewById(R.id.editTextQuantidade);

       textViewValorTotal         =(TextView) this.findViewById(R.id.textViewValorTotal);

        textViewValorDevendo      = (TextView) this.findViewById(R.id.textViewValorDevendo);

        editTextValorPago      =(EditText) this.findViewById(R.id.editTextValorPago);

        buttonSalvar           = (Button) this.findViewById(R.id.buttonSalvar);

        buttonVoltar           = (Button) this.findViewById(R.id.buttonVoltar);

        produtoRepository =  new ProdutoRepository(this);


    }
    //CRIA OS EVENTOS DOS COMPONENTES
    protected  void CriarEventos() {
        //CRIANDO EVENTO NO BOTÃO SALVAR
        buttonSalvar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Salvar_onClick();
            }
        });

        //CRIANDO EVENTO NO BOTÃO VOLTAR
        buttonVoltar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intentMainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentMainActivity);
                finish();
            }
        });
        editTextValorPago.setText("0.0");
        editTextQuantidade.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editTextQuantidade.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (editTextQuantidade.getText().toString().equals(""))
                        editTextQuantidade.setText("1");
                    atualizar_pagina();
                    return true;
                }
                return false;
            }
        });
        editTextValorPago.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editTextValorPago.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (editTextQuantidade.getText().toString().equals(""))
                        editTextQuantidade.setText("0.0");
                    atualizar_pagina();
                    return true;
                }
                return false;
            }
        });
        spinnerProduto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                atualizar_pagina();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
   }

    //VALIDA OS CAMPOS E SALVA AS INFORMAÇÕES NO BANCO DE DADOS
    protected  void Salvar_onClick(){

            /*CRIANDO UM OBJETO PESSOA*/
            VendaModel pessoaModel = new VendaModel();

            /*SETANDO O VALOR DO CAMPO NOME*/
            pessoaModel.setQuantidade(Integer.valueOf(editTextQuantidade.getText().toString().trim()));

            pessoaModel.setDs_Nome_cliente(spinnerCliente.getSelectedItem().toString());

            pessoaModel.setDs_Nome_produto(spinnerProduto.getSelectedItem().toString());

            String[] aux = textViewValorDevendo.getText().toString().split(" ");
            Float divida = Float.parseFloat(aux[1]);
            pessoaModel.setSaldo(divida);

            aux = textViewValorTotal.getText().toString().split(" ");
            Float valor_total = Float.parseFloat(aux[1]);
            pessoaModel.setValor_total(valor_total);

            pessoaModel.setValor_pago(Float.parseFloat(editTextValorPago.getText().toString()));

            /*SALVANDO UM NOVO REGISTRO*/
            new VendaRepository(this).Salvar(pessoaModel);

            /*MENSAGEM DE SUCESSO!*/
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            //ADICIONANDO UM TITULO A NOSSA MENSAGEM DE ALERTA
            alertDialog.setTitle(R.string.app_name);

            //MENSAGEM A SER EXIBIDA
            alertDialog.setMessage("Venda cadastrada com sucesso! ");

            //CRIA UM BOTÃO COM O TEXTO OK SEM AÇÃO
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {

                    //RETORNA PARA A TELA DE CONSULTA
                    Intent intentRedirecionar = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intentRedirecionar);
                    finish();
                }
            });

             //MOSTRA A MENSAGEM NA TELA
             alertDialog.show();

            LimparCampos();



    }

    //LIMPA OS CAMPOS APÓS SALVAR AS INFORMAÇÕES
    protected void LimparCampos(){
        editTextQuantidade.setText("1");
        textViewValorTotal.setText("R$ 0.0");
        textViewValorDevendo.setText("R$ 0.0");
        editTextValorPago.setText("0.0");
    }

    protected void popularSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.spinnerCliente);

        ClienteRepository pessoaRepository =  new ClienteRepository(this);

        //BUSCA AS PESSOAS CADASTRADAS
        List<String> pessoas = pessoaRepository.SelecionarNomes();

        ArrayAdapter<String> adapterr = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, pessoas);

        adapterr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterr);

        Spinner spinner2 = (Spinner) findViewById(R.id.spinnerProduto);

        //BUSCA AS PESSOAS CADASTRADAS
        List<String> produtos = produtoRepository.SelecionarNomes();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, produtos);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter);
    }
    public void atualizar_pagina(){

            String produto = spinnerProduto.getSelectedItem().toString();
            int quantidade = Integer.parseInt(String.valueOf(editTextQuantidade.getText()));
            Float preço = produtoRepository.consultarPreço(produto, quantidade);
            textViewValorTotal.setText("R$ " + preço);
            String[] aux = textViewValorDevendo.getText().toString().split(" ");
            Float divida = Float.parseFloat(aux[1]);
            Float valorpago = Float.parseFloat(editTextValorPago.getText().toString());
            divida = preço - valorpago;
            textViewValorDevendo.setText("R$ " + divida);

            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);


    }
}



