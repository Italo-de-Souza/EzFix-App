package com.ezfix.ezfixaplication.mainscreen

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.ezfix.ezfixaplication.ActivityInicial
import com.ezfix.ezfixaplication.configuration.Constants
import com.ezfix.ezfixaplication.configuration.HttpRequest
import com.ezfix.ezfixaplication.databinding.FragmentMainPerfilBinding
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ezfix.ezfixaplication.databinding.FragmentCadastroContaBinding
import com.ezfix.ezfixaplication.databinding.FragmentCadastroDadosBinding
import com.ezfix.ezfixaplication.databinding.FragmentCadastroEnderecoBinding
import okhttp3.internal.Util
import java.util.jar.Manifest

class FragmentMainPerfil : Fragment() {

    private lateinit var binding            : FragmentMainPerfilBinding;
    private lateinit var imgPerfil          : ImageView;
    private lateinit var tvNomePerfil       : TextView;
    private lateinit var cvEmailSenha       : CardView;
    private lateinit var cvDadosPessoais    : CardView;
    private lateinit var cvEndereco         : CardView;
    private lateinit var incEmailSenha      : FragmentCadastroContaBinding;
    private lateinit var incDadosPessoais   : FragmentCadastroDadosBinding;
    private lateinit var incEndereco        : FragmentCadastroEnderecoBinding;
    private lateinit var btnSair : Button;
    private lateinit var preferencias : SharedPreferences;


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMainPerfilBinding.inflate(layoutInflater);
        val view = binding.root;

        imgPerfil           = binding.imgPerfil;
        tvNomePerfil        = binding.tvNomePerfil;
        cvEmailSenha        = binding.cvEmailSenha;
        cvDadosPessoais     = binding.cvDadosPessoais;
        cvEndereco          = binding.cvEndereco;
        incEmailSenha       = binding.incEmailSenha;
        incDadosPessoais    = binding.incDadosPessoais;
        incEndereco         = binding.incEndereco;
        btnSair             = binding.btnSair;

        preferencias = requireActivity().getSharedPreferences("token", AppCompatActivity.MODE_PRIVATE)

        cvEmailSenha.setOnClickListener {
            if (incEmailSenha.root.visibility == View.VISIBLE){
                incEmailSenha.root.visibility = View.GONE
            } else{
                incEmailSenha.root.visibility = View.VISIBLE
            }
        }
        cvDadosPessoais.setOnClickListener {
            if (incDadosPessoais.root.visibility == View.VISIBLE){
                incDadosPessoais.root.visibility = View.GONE
            } else{
                incDadosPessoais.root.visibility = View.VISIBLE
            }
        }
       cvEndereco.setOnClickListener {
            if (incEndereco.root.visibility == View.VISIBLE){
                incEndereco.root.visibility = View.GONE
            } else{
                incEndereco.root.visibility = View.VISIBLE
            }
        }



//        ABRIR GALERIA
        val novaImagem = registerForActivityResult(ActivityResultContracts
            .StartActivityForResult()){ result ->
            if (result.resultCode == RESULT_OK){
                val uriImage = result.data?.data;
                atualizaImagem(uriImage);
            }
        }


//      ONCLICK DA IMAGEM
        imgPerfil.setOnClickListener {
            if (!isPermitted()){
                requestPermission()
            }
            else{
                val intent = Intent(Intent.ACTION_PICK);
                intent.type = "image/*"
                novaImagem.launch(intent);
            }
        }

        btnSair.setOnClickListener{ efetuarLogOut() }

        setInfos();
        getFotoPerfil();

        return view;
    }


//    BUSCA FOTO PERFIL
    private fun getFotoPerfil(){
        val http = HttpRequest.requerir();
        http.getFotoPerfil(Constants.userLogado.cpf).enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val bmp = BitmapFactory.decodeStream(response.body()!!.byteStream());
                imgPerfil.setImageBitmap(bmp);
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(context, "${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

//    ATUALIZA IMAGEM PERFIL
    private fun atualizaImagem(uriImage: Uri?) {
        val file = File(getRealPathFromURI(uriImage!!));
        val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        val parts = MultipartBody.Part.createFormData("img", file.name, requestBody)
        val token = Constants.token;

        val http = HttpRequest.requerir();
        http.pathFotoPerfil(parts, "${token.tipo} ${token.token}").enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) getFotoPerfil()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(context, "${t.message}", Toast.LENGTH_LONG).show();
            }
        })
    }

//    PEGA URI DA FOTO
    fun getRealPathFromURI(contentUri: Uri): String? {
        var res: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context?.contentResolver?.query(contentUri, proj, null, null, null)
        if (cursor!!.moveToFirst()) {
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            res = cursor.getString(column_index)
        }
        cursor.close()
        return res
    }


//    SETA AS INFORMAÇÕES DO PERFIL
    private fun setInfos(){
        val usuario = Constants.userLogado
        val endereco = Constants.userLogado.enderecoEspecificos;


        tvNomePerfil.text = usuario.nome;
        incEmailSenha.etEmailCadastro.setText(usuario.email);
        incDadosPessoais.etNome.setText(usuario.nome);
        incDadosPessoais.etCpf.setText(usuario.cpf);
//        incDadosPessoais.etNascimento.setText(usuario.dataNascimento);
        incDadosPessoais.etTelefonePrimario.setText(usuario.telefonePrimario);
        incDadosPessoais.etTelefoneSecundario.setText(usuario.telefoneSecundario);

//        ENDEREÇO
        incEndereco.etBairro.setText(endereco[0].enderecoGeral.bairro);
        incEndereco.etCep.setText(endereco[0].enderecoGeral.cep.toString());
        incEndereco.etCidade.setText(endereco[0].enderecoGeral.cidade);
        incEndereco.etEstado.setText(endereco[0].enderecoGeral.estado);
        incEndereco.etLogradouro.setText(endereco[0].enderecoGeral.logradouro);
        incEndereco.etComplemento.setText(endereco[0].complemento);
        incEndereco.etNumero.setText(endereco[0].numero.toString());

//        DESABILITA CAMPOS
        incEmailSenha.etEmailCadastro.isEnabled = false;
        incDadosPessoais.etNome.isEnabled = false;
        incDadosPessoais.etCpf.isEnabled = false;
        incDadosPessoais.etNascimento.isEnabled = false;
        incDadosPessoais.etTelefonePrimario.isEnabled = false;
        incDadosPessoais.etTelefoneSecundario.isEnabled = false;

        incEndereco.etBairro.isEnabled = false;
        incEndereco.etCep.isEnabled = false;
        incEndereco.etCidade.isEnabled = false;
        incEndereco.etComplemento.isEnabled = false;
        incEndereco.etEstado.isEnabled = false;
        incEndereco.etLogradouro.isEnabled = false;
        incEndereco.etNumero.isEnabled = false;

//        ESCONDENDO CAMPOS
        incEmailSenha.etSenhaCadastro1.visibility = View.GONE;
        incEmailSenha.etSenhaCadastro2.visibility = View.GONE;
        incDadosPessoais.etNascimento.visibility = View.GONE;


    }

//    LOGOFF
    private fun efetuarLogOut(){
        val tokenOff = preferencias.edit().clear();
        tokenOff.commit();
        startActivity(Intent(context, ActivityInicial::class.java));
        activity?.finish()
    }

    private fun isPermitted(): Boolean{
        return ContextCompat.checkSelfPermission(
            requireActivity().baseContext, android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            requireActivity(),
        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.MANAGE_EXTERNAL_STORAGE),
        READ_EXTERNAL_STORAGE_PERMISSION_CODE)
    }

    companion object{
        private const val READ_EXTERNAL_STORAGE_PERMISSION_CODE = 1000;
    }


}