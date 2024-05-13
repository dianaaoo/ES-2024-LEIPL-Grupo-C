package lei_pl_grupo_c;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class ScheduleEntry {

    private String curso;
    private String unidadeCurricular;
    private String turno;
    private String turma;
    private int inscritos;
    private String diaSemana;
    private String horaInicio;
    private String horaFim;
    private String dataAula;
    private String caracteristicasSala;
    private String salaAtribuida;
    private int semanaAno; // Calculated week of the year
    private int semanaSemestre; // Calculated week of the semester (assuming 15 weeks)

    // Getters and Setters for each field
    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getUnidadeCurricular() {
        return unidadeCurricular;
    }

    public void setUnidadeCurricular(String unidadeCurricular) {
        this.unidadeCurricular = unidadeCurricular;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getTurma() {
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    public int getInscritos() {
        return inscritos;
    }

    public void setInscritos(int inscritos) {
        this.inscritos = inscritos;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(String horaFim) {
        this.horaFim = horaFim;
    }

    public String getDataAula() {
        return dataAula;
    }

    public void setDataAula(String dataAula) {
        this.dataAula = dataAula;
    }

    public String getCaracteristicasSala() {
        return caracteristicasSala;
    }

    public void setCaracteristicasSala(String caracteristicasSala) {
        this.caracteristicasSala = caracteristicasSala;
    }

    public String getSalaAtribuida() {
        return salaAtribuida;
    }

    public void setSalaAtribuida(String salaAtribuida) {
        this.salaAtribuida = salaAtribuida;
    }

    public int getSemanaAno() {
        return semanaAno;
    }

    public void setSemanaAno(int semanaAno) {
        this.semanaAno = semanaAno;
    }

    public int getSemanaSemestre() {
        return semanaSemestre;
    }

    public void setSemanaSemestre(int semanaSemestre) {
        this.semanaSemestre = semanaSemestre;
    }

    // Method to calculate semanaAno and semanaSemestre based on dataAula
    public void calculateWeeks() {
        LocalDate date = LocalDate.parse(dataAula); // Assuming dataAula is in ISO-8601 format (yyyy-MM-dd)
        
        // Calculating week of the year
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        semanaAno = date.get(weekFields.weekOfWeekBasedYear());
        
        // Calculating week of the semester
        int startWeek = 1; // Assuming semester starts in week 1
        int endWeek = 15; // Assuming semester ends in week 15
        int daysSinceStart = date.getDayOfYear();
        semanaSemestre = (daysSinceStart / 7) - ((startWeek * 7) / 7) + 1;
    }
}
