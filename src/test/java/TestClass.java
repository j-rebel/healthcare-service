import org.junit.Test;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.alert.SendAlertServiceImpl;
import ru.netology.patient.service.medical.MedicalService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TestClass {

    public static final BloodPressure normalBP = new BloodPressure(60, 120);
    public static final BloodPressure abnormalBP = new BloodPressure(120, 200);
    public static final BigDecimal normalTemperature = new BigDecimal("36.6");
    public static final BigDecimal abnormalTemperature = new BigDecimal("90.8");

    public static final PatientInfo patient = new PatientInfo("id", "name", "surname", LocalDate.now(), new HealthInfo(normalTemperature, normalBP));

    @Test
    public void test_message_sending() {
        SendAlertService sas = Mockito.mock(SendAlertServiceImpl.class);
        Mockito.doNothing().when(sas).send(Mockito.isA(String.class));
        sas.send("test");
        Mockito.verify(sas, Mockito.times(1)).send("test");
    }

    @Test
    public void check_message_when_pressure_within_norm() {
        PatientInfoRepository pir = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(pir.getById("id")).thenReturn(patient);
        SendAlertService sas = Mockito.mock(SendAlertServiceImpl.class);
        Mockito.doNothing().when(sas).send(Mockito.isA(String.class));

        MedicalService ms = new MedicalServiceImpl(pir, sas);
        ms.checkBloodPressure("id", normalBP);

        Mockito.verify(sas, Mockito.never()).send("Warning, patient with id: id, need help");
    }

    @Test
    public void check_message_when_pressure_out_of_norm() {
        PatientInfoRepository pir = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(pir.getById("id")).thenReturn(patient);
        SendAlertService sas = Mockito.mock(SendAlertServiceImpl.class);
        Mockito.doNothing().when(sas).send(Mockito.isA(String.class));

        MedicalService ms = new MedicalServiceImpl(pir, sas);
        ms.checkBloodPressure("id", abnormalBP);

        Mockito.verify(sas, Mockito.times(1)).send("Warning, patient with id: id, need help");
    }

    @Test
    public void check_message_when_temperature_within_norm() {
        PatientInfoRepository pir = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(pir.getById("id")).thenReturn(patient);
        SendAlertService sas = Mockito.mock(SendAlertServiceImpl.class);
        Mockito.doNothing().when(sas).send(Mockito.isA(String.class));

        MedicalService ms = new MedicalServiceImpl(pir, sas);
        ms.checkTemperature("id", normalTemperature);

        Mockito.verify(sas, Mockito.never()).send("Warning, patient with id: id, need help");
    }

    @Test
    public void check_message_when_temperature_out_of_norm() {
        PatientInfoRepository pir = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(pir.getById("id")).thenReturn(patient);
        SendAlertService sas = Mockito.mock(SendAlertServiceImpl.class);
        Mockito.doNothing().when(sas).send(Mockito.isA(String.class));

        MedicalService ms = new MedicalServiceImpl(pir, sas);
        ms.checkTemperature("id", abnormalTemperature);
        Mockito.verify(sas, Mockito.times(1)).send("Warning, patient with id: id, need help");
    }


}
