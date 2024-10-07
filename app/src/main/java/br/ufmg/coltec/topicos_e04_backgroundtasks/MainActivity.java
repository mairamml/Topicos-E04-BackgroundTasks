package br.ufmg.coltec.topicos_e04_backgroundtasks;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button downloadBtn = findViewById(R.id.btn_download);
        EditText txtLink = findViewById(R.id.txt_img_link);
        ImageView imgView = findViewById(R.id.img_picture);
        ProgressBar progress = findViewById(R.id.progress);

//        TODO[1]: Processo de download e carregamento da imagem acontecendo na Main Thread, ALTERAR!!
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO[2]: Exibir barra de progresso quando estiver fazendo download da imagem
                progress.setVisibility(View.VISIBLE);
                imgView.setVisibility(View.GONE);
                String url = txtLink.getText().toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //para executar em background
                        Bitmap img = null;
                        try {
                            img = ImageDownloader.download(url);
                            if(img != null){
                                imgView.setImageBitmap(img);
                                imgView.setVisibility(View.VISIBLE);
                                progress.setVisibility(View.GONE);
                            }else{
                                Log.e("MainActivity", "Image couldn't be loaded");
                            }
                        } catch (IOException e) {

                            runOnUiThread(() -> {
                                progress.setVisibility(View.GONE);
                            });
                        }
                    }
                }).start();
            }
        });
    }

    private Bitmap downloadImage(String imgLink) {
        try {
            return ImageDownloader.download(imgLink);
        } catch (IOException e) {
            Log.e("MainActivity", e.toString());
            return null;
        }
    }
}