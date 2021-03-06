package com.ezfix.ezfixaplication.mainscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ezfix.ezfixaplication.R
import com.ezfix.ezfixaplication.databinding.ActivityMainBinding

class ActivityMain : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding;
    //Definindo fragments
    private lateinit var fragmentMainHome : FragmentMainHome;
    private lateinit var fragmentMainBuscar : FragmentMainBuscar;
    private lateinit var fragmentMainPedidos : FragmentMainPedidos;
    private lateinit var fragmentMainPerfil : FragmentMainPerfil;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater);
        //Instanciando fragments
        fragmentMainHome = FragmentMainHome();
        fragmentMainBuscar = FragmentMainBuscar();
        fragmentMainPerfil = FragmentMainPerfil();
        fragmentMainPedidos = FragmentMainPedidos();

        val view = binding.root;

        //Onclick do MENU
        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.im_home -> { setFragment(fragmentMainHome); true }
                R.id.im_buscar -> { setFragment(fragmentMainBuscar); true }
                R.id.im_pedidos -> { setFragment(fragmentMainPedidos); true }
                R.id.im_profile -> { setFragment(fragmentMainPerfil); true }
                else -> false;
            }
        }

        if (savedInstanceState == null) {
            setFragment(fragmentMainHome);
        }

        setContentView(view);
    }

    private fun setFragment(fragment : Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(binding.flMain.id, fragment).commit();
    }
}