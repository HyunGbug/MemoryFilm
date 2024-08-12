package com.smile.www.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;

import com.smile.www.service.PhotoService;

@MultipartConfig
public class PhotoController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PhotoService photoService;

	public PhotoController() {
		super();
		photoService = new PhotoService();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		 try {
	            processRequest(request, response);
	        } catch (FileSizeLimitExceededException e) {
	            handleFileSizeLimitExceededException(e, request, response);
	        } catch (SizeLimitExceededException e) {
	            handleSizeLimitExceededException(e, request, response);
	        }
	    }

	private void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getServletPath();
		System.out.println(action);

		if (action == null || action.equals("/")) {
			action = "/list.photo"; // 기본 경로 설정
		}
		String viewPage = null;

		try {
			switch (action) {
			case "/upload.photo":
				viewPage = photoService.addPhoto(request, response);
				break;
			case "/list.photo":
				viewPage = photoService.listPhotos(request);
				break;
			case "/view.photo":
				photoService.viewPhoto(request, response);
				return;
			case "/savePhotoDetails.photo":
				photoService.savePhotoDetails(request, response);
				return;
			case "/delete.photo":
				photoService.deletePhoto(request, response);
				return;
			case "/deleteSelectedPhotos.photo":
				photoService.deleteSelectedPhotos(request, response);
				return;
			case "/download.photo":
				photoService.downloadPhoto(request, response);
				return;
			case "/downloadSelectedPhotos.photo":
				photoService.downloadSelectedPhotos(request, response);
				return;
			case "/photoCount.photo":
				photoService.getPhotoCount(request, response);
				return;
			default:
				viewPage = "error.jsp";
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			viewPage = "error.jsp";
		}

		if (viewPage != null && viewPage.startsWith("/")) {
			response.sendRedirect(request.getContextPath() + viewPage);
		} else {
			RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
			dispatcher.forward(request, response);
		}
	}
	 private void handleFileSizeLimitExceededException(FileSizeLimitExceededException e, HttpServletRequest request,
	            HttpServletResponse response) throws IOException {
	        response.sendRedirect(request.getContextPath() + "/fileSizeExceeded");
	    }

	    private void handleSizeLimitExceededException(SizeLimitExceededException e, HttpServletRequest request,
	            HttpServletResponse response) throws IOException {
	        response.sendRedirect(request.getContextPath() + "/sizeLimitExceeded");
	    }
}
