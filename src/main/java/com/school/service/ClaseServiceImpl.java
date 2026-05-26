package com.school.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.school.dao.MaterialDao;
import com.school.dao.NotaDao;
import com.school.model.Asignacion;
import com.school.model.Aula;
import com.school.model.Nota;
import com.school.reportDto.AsistenciaReporte;
import com.school.reportDto.CursoReporte;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.school.dao.AulaDao;
import com.school.dao.ClaseDao;
import com.school.model.Clase;

@Service
public class ClaseServiceImpl implements ClaseService{

	@Autowired
	private ClaseDao claseDao;
	
	@Autowired
	private AulaDao aulaDao;

	@Autowired
	private NotaDao notaDao;

	@Override
	@Transactional
	public Clase save(Clase clase) {
		// TODO Auto-generated method stub
		clase.setAula(aulaDao.findById(clase.getAula().getId()).get());
		clase.getFrecuencias().stream().forEach(f -> f.setClase(clase));
		return claseDao.save(clase);
	}

	@Override
	public Clase saveNoFrecuenciaUpdate(Clase clase) {
		return claseDao.save(clase);
	}


	@Override
	@Transactional
	public Clase update(Clase clase) {
		// TODO Auto-generated method stub
		clase.getFrecuencias().stream().forEach(f -> f.setClase(clase));
		return claseDao.save(clase);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Clase> getClaseById(Long id) {
		// TODO Auto-generated method stub
		return claseDao.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Clase> findAll() {
		// TODO Auto-generated method stub
		return (List<Clase>) claseDao.findAll();
	}

	@Override
	@Transactional
	public boolean delete(Long id) {
		// TODO Auto-generated method stub
		return getClaseById(id).map(c -> {
			claseDao.deleteById(id);
			return true;
		}).orElse(false);
	}

	@Override
	public byte[] generarReporteCurso(String tipo, Long idCurso, Long idGrado) {
		byte[] data = null;

		List<CursoReporte> listacursoReporte = getCursoReporte(idCurso,idGrado);

		if(listacursoReporte == null) return data;

		try {
			File file = new File(getClass().getClassLoader().getResource("cursoReporte.jasper").getFile());

			JasperPrint rpt = JasperFillManager.fillReport(file.getPath(), null, new JRBeanCollectionDataSource(listacursoReporte));

			if(tipo.equals("pdf")){
				data = JasperExportManager.exportReportToPdf(rpt);
			}else{
				SimpleXlsxReportConfiguration config = new SimpleXlsxReportConfiguration();
				config.setOnePagePerSheet(true);
				config.setIgnoreGraphics(false);

				ByteArrayOutputStream out = new ByteArrayOutputStream();

				Exporter exporter =  new JRXlsxExporter();
				exporter.setExporterInput(new SimpleExporterInput(rpt));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
				exporter.exportReport();

				data = out.toByteArray();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}


	@Override
	public List<CursoReporte> getCursoReporte(Long idCurso, Long idGrado) {

		List<Aula> aulasPorGrado = null;
		List<Aula> aulasGeneral = null;
		List<CursoReporte> cursoReporteList = new ArrayList<>();

		if(idGrado == 0){
			aulasGeneral = (List<Aula>) aulaDao.findAll();

			cursoReporteList = buscarAprobadosYDesaprobados(aulasGeneral, idCurso);

			return cursoReporteList;

		}
		aulasPorGrado = aulaDao.findAulaPorGrado(idGrado);
		cursoReporteList = buscarAprobadosYDesaprobados(aulasPorGrado, idCurso);


		return cursoReporteList;
	}

	@Override
	public List<Asignacion> asignacionesPorClase(Long idClase) {
		return claseDao.asignacionesPorClase(idClase);
	}

	private List<CursoReporte> buscarAprobadosYDesaprobados(List<Aula> aulas, Long idCurso){
		List<CursoReporte> cursoReporteList = new ArrayList<>();

		for(Aula aula: aulas){
			long aprobados = 0;
			long desaprobados = 0;
			Clase clase = claseDao.findClasePorAulaYCurso(idCurso ,aula.getId());
			if(clase != null){
				List<Nota> notas = notaDao.notasPorAulaYCurso(idCurso, aula.getId());

				if(notas.size() == 0){
					return cursoReporteList;
				}
				aprobados = notas.stream().filter(n -> n.getNota_bim1() > 12).count();
				desaprobados = notas.size() - aprobados;
				CursoReporte cursoReporte = new CursoReporte(notas.get(0).getEstudiante().getAulaEstudiante().getNombre() + notas.get(0).getEstudiante().getAulaEstudiante().getSeccion(),notas.get(0).getCurso().getNombre() , aprobados, desaprobados);
				cursoReporteList.add(cursoReporte);

			}
		}

		return cursoReporteList;
	}

}
