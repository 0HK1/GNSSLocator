package com.example.GNSSLocator;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class GNSSActivity extends AppCompatActivity {
    private BarChartView barChartView;
    private LocationManager locationManager;
    private LocationProvider locationProvider;
    private static final int REQUEST_LOCATION = 1;
    private MapaEstelar mapaEstelar;
    private final List<float[]> satelitesData = new ArrayList<>();
    private final List<Boolean> satelitesInFix = new ArrayList<>();
    private final List<Float> newValues = new ArrayList<>();
    private final List<Float> svid = new ArrayList<>();
    private int radioButtonId = 1;
    private int cases;
    private boolean checkedGPS, checkedGalileo, checkedGlonas, checkedInFix;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_gnssactivity);
        mapaEstelar = findViewById(R.id.MapaEstelar);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        obtemLocationProvider_Permission();
        TextView textView = findViewById(R.id.textviewLocation_id);
        checkedGalileo = checkedGlonas = checkedGPS = checkedInFix = true;
        TextView textQualidadeSinalGNSS = findViewById(R.id.qualidadeSinal);
        barChartView = findViewById(R.id.barChart);

        //Ativa os Dialogos
        textView.setOnClickListener(view -> showRadioDialog());

        mapaEstelar.setOnClickListener(view -> showFiltroSatelite());

        textQualidadeSinalGNSS.setOnClickListener(view -> mostrarGrafico());


    }
    //Dialogo do gráfico
    private void mostrarGrafico() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.grafico_qualidadesinal);

        barChartView = dialog.findViewById(R.id.barChart);

        if (newValues != null && !newValues.isEmpty()) {
            barChartView.setValues(newValues, svid);
        }
        dialog.show();


    }
    //Dialogo das definições de formatação do texto de localização
    private void showRadioDialog() {
        SharedPreferences sharedPreferences = getSharedPreferences("GNSS_PREFERENCES", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.buttonsgraus);

        // Obtendo o tipo de formato salvo nas preferências
        int tipoFormat = sharedPreferences.getInt("typeFormat", 0);  // Valor padrão 0 caso não tenha sido salvo

        // Inicializa o grupo de botões de rádio
        RadioGroup radioGroup = dialog.findViewById(R.id.radioGroupGraus);

        // Selecionando o botão de rádio correspondente ao tipo de formato salvo
        switch (tipoFormat) {
            case 0:
                radioButtonId = R.id.Graus;
                break;
            case 1:
                radioButtonId = R.id.Graus_Minutos;
                break;
            case 2:
                radioButtonId = R.id.Graus_Minutos_Segundos;
                break;
            default:
                radioButtonId = R.id.Graus;  // Valor padrão se não for encontrado
                break;
        }

        // Marcando o botão de rádio selecionado
        RadioButton selectedRadioButton = dialog.findViewById(radioButtonId);
        if (selectedRadioButton != null) {
            selectedRadioButton.setChecked(true);
        }

        // Listener para alteração de seleção
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.Graus) {
                editor.putInt("typeFormat", 0);
                cases = 0;
            } else if (checkedId == R.id.Graus_Minutos) {
                editor.putInt("typeFormat", 1);
                cases = 1;
            } else if (checkedId == R.id.Graus_Minutos_Segundos) {
                editor.putInt("typeFormat", 2);
                cases = 2;
            }
            editor.apply();
            dialog.dismiss();
        });

        dialog.show();
    }

    //Dialogo de filtro de satelites
    private void showFiltroSatelite() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.filtro_satelite);

        //Rotina para orientar a ordem do checkbox
        CheckBox checkBoxGPS = dialog.findViewById(R.id.checkBoxGPS);
        if (checkedGPS){
            checkBoxGPS.setChecked(true);
        }
        CheckBox checkBoxGalileo = dialog.findViewById(R.id.checkBoxGalileo);
        if (checkedGalileo){
            checkBoxGalileo.setChecked(true);
        }
        CheckBox checkBoxGlonass = dialog.findViewById(R.id.checkBoxGlonass);
        if (checkedGlonas){
            checkBoxGlonass.setChecked(true);
        }
        CheckBox checkBoxInFix = dialog.findViewById(R.id.checkBoxInFix);
        if (checkedInFix){
            checkBoxInFix.setChecked(true);
        }
        //Implementação dos filtros por meio de um boolean
        checkBoxGPS.setOnCheckedChangeListener((buttonView, isChecked) -> checkedGPS = isChecked);

        checkBoxGalileo.setOnCheckedChangeListener((buttonView, isChecked) -> checkedGalileo = isChecked);

        checkBoxGlonass.setOnCheckedChangeListener((buttonView, isChecked) -> checkedGlonas = isChecked);

        checkBoxInFix.setOnCheckedChangeListener((buttonView, isChecked) -> checkedInFix = isChecked);

        dialog.show();
    }
    //obtenção e verificação das permissões de acesso a localização do dispositivo
    public void obtemLocationProvider_Permission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
            startLocationAndGNSSUpdates();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION && grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            obtemLocationProvider_Permission();
        } else {
            Toast.makeText(this, "Sem permissão para acessar o sistema de posicionamento", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void startLocationAndGNSSUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager.requestLocationUpdates(locationProvider.getName(), 1000, 0.1f, this::mostraLocation);

        locationManager.registerGnssStatusCallback(new GnssStatus.Callback() {
            @Override
            public void onSatelliteStatusChanged(@NonNull GnssStatus status) {
                super.onSatelliteStatusChanged(status);
                mostraGNSS(status);
            }
        });
    }
    //Tratamento das informações dos Satelites
    public void mostraGNSS(GnssStatus status) {

        String qualidadeSinal = "";
            satelitesInFix.clear();
            satelitesData.clear();
            newValues.clear();
            svid.clear();

            if (status != null) {

                for (int i = 0; i < status.getSatelliteCount(); i++) {

                    float azimuth = status.getAzimuthDegrees(i);
                    float elevation = status.getElevationDegrees(i);
                    float Svid = status.getSvid(i);
                    float Contelacao = status.getConstellationType(i);

                    if(checkedInFix && status.usedInFix(i)) {
                        if (checkedGPS && Contelacao == 1) {
                            satelitesData.add(new float[]{azimuth, elevation, Svid, Contelacao});
                            satelitesInFix.add(status.usedInFix(i));
                            qualidadeSinal += "Svid:" + status.getSvid(i) + "\n" + "Hz" + status.getCn0DbHz(i) + "\n";
                            newValues.add(status.getCn0DbHz(i));
                            svid.add(Svid);

                        }
                        if (checkedGlonas && Contelacao == 3) {
                            satelitesData.add(new float[]{azimuth, elevation, Svid, Contelacao});
                            satelitesInFix.add(status.usedInFix(i));
                            qualidadeSinal += "Svid:" + status.getSvid(i) + "\n" + "Hz" + status.getCn0DbHz(i) + "\n";
                            newValues.add(status.getCn0DbHz(i));
                            svid.add(Svid);
                        }
                        if (checkedGalileo && Contelacao == 6) {
                            satelitesData.add(new float[]{azimuth, elevation, Svid, Contelacao});
                            satelitesInFix.add(status.usedInFix(i));
                            qualidadeSinal += "Svid:" + status.getSvid(i) + "\n" + "Hz" + status.getCn0DbHz(i) + "\n";
                            newValues.add(status.getCn0DbHz(i));
                            svid.add(Svid);
                        }
                    }
                    if(!checkedInFix && !status.usedInFix(i)){
                        if (checkedGPS && Contelacao == 1) {
                            satelitesData.add(new float[]{azimuth, elevation, Svid, Contelacao});
                            satelitesInFix.add(status.usedInFix(i));
                            qualidadeSinal += "Svid:" + status.getSvid(i) + "\n" + "Hz" + status.getCn0DbHz(i) + "\n";
                            newValues.add(status.getCn0DbHz(i));
                            svid.add(Svid);
                        }
                        if (checkedGlonas && Contelacao == 3) {
                            satelitesData.add(new float[]{azimuth, elevation, Svid, Contelacao});
                            satelitesInFix.add(status.usedInFix(i));
                            qualidadeSinal += "Svid:" + status.getSvid(i) + "\n" + "Hz" + status.getCn0DbHz(i) + "\n";
                            newValues.add(status.getCn0DbHz(i));
                            svid.add(Svid);
                        }
                        if (checkedGalileo && Contelacao == 6) {
                            satelitesData.add(new float[]{azimuth, elevation, Svid, Contelacao});
                            satelitesInFix.add(status.usedInFix(i));
                            qualidadeSinal += "Svid:" + status.getSvid(i) + "\n" + "Hz" + status.getCn0DbHz(i) + "\n";
                            newValues.add(status.getCn0DbHz(i));
                            svid.add(Svid);
                        }
                    }

                }
                if (mapaEstelar != null) {
                    mapaEstelar.updateSatelliteData(satelitesData, satelitesInFix);
                    barChartView.setValues(newValues, svid);
                }
            }

        }
    //Tratamento das informações da localização do usuário
    public void mostraLocation(Location location) {
        TextView textView = findViewById(R.id.textviewLocation_id);
        String mens = getString(R.string.ultimaPosicao_string) + "\n";

        if (location != null) {
            switch (cases){
                case 1:
                    mens += getString(R.string.grausLatitudeMinuto_string) + "\n" + Location.convert(location.getLatitude(), Location.FORMAT_MINUTES) + "\n"
                            + getString(R.string.grausLongituteMinutos_string) + "\n" + Location.convert(location.getLongitude(), Location.FORMAT_MINUTES) + "\n"
                            + getString(R.string.altitude_string) + location.getAltitude() + "\n"
                            + getString(R.string.rumo_string) + location.getBearing()+ "\n";
                    break;
                case 2:
                    mens += getString(R.string.grausLatitudeMinutoSegundo_string) + "\n" + Location.convert(location.getLatitude(), Location.FORMAT_SECONDS) + "\n"
                            + getString(R.string.grausLongituteMinutosSegundos_string) + "\n" + Location.convert(location.getLongitude(), Location.FORMAT_SECONDS) + "\n"
                            + getString(R.string.altitude_string) + location.getAltitude() + "\n"
                            + getString(R.string.rumo_string) + location.getBearing()+ "\n";
                    break;
                case 0:
                default:
                    mens += getString(R.string.grausLatitude_string) + "\n" + Location.convert(location.getLatitude(), Location.FORMAT_DEGREES) + "\n"
                            + getString(R.string.grausLongitute_string) + "\n" + Location.convert(location.getLongitude(), Location.FORMAT_DEGREES) + "\n"
                            + getString(R.string.altitude_string) + location.getAltitude() + "\n"
                            + getString(R.string.rumo_string) + location.getBearing()+ "\n";
                    break;
            }

        } else {
            mens += "Localização Não disponível";
        }
        textView.setText(mens);
    }
}