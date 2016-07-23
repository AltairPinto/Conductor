package br.com.cardtracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by altai on 23/07/2016.
 */
public class LoginFragment extends DialogFragment {
    private EditText editSenha;
    public static int verifica;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Cria o objeto para configurar o AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Define o layout do AlertDialog
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.login, null);
        builder.setView(dialogView);

        // Título do AlertDialog
        builder.setTitle("Digite o Código de Confirmação");

        // Define os campos de usuário e senha
        editSenha = (EditText)dialogView.findViewById(R.id.textSenha);

        // Configura o botão de Ok
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Se digitada a senha 5530928 informa que a
                // senha está correta
                if(editSenha.getText().toString().equals("5530928")){
                    Toast.makeText(getActivity(), "Código correto",
                            Toast.LENGTH_SHORT).show();
                            verifica=1;
                            setPress(verifica);
                            dismiss();
                }
                else {
                    Toast.makeText(getActivity(), "Código incorreto",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Configura o botão de Cancelar
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Fecha o AlertDialog
                dismiss();
            }
        });

        // Cria o objeto AlertDialog
        return builder.create();

    }
    public int getPress(){System.out.println("valor retornado : "+verifica);return verifica;}
    public void setPress(int verifica){this.verifica = verifica;}

}
