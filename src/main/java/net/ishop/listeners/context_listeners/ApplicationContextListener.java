package net.ishop.listeners.context_listeners;

import net.ishop.entities.Category;
import net.ishop.entities.Producer;
import net.ishop.models.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.ishop.services.impl.ServiceManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;

@WebListener
public class ApplicationContextListener implements ServletContextListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationContextListener.class);
    private ServiceManager serviceManager;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            serviceManager = ServiceManager.getInstance(sce.getServletContext());
            List<Category> allCategoriesList = serviceManager.getProductService().getAllCategoriesList();
            List<Producer> allProducersList = serviceManager.getProductService().getAllProducersList();
            sce.getServletContext().setAttribute(Constants.CATEGORY_LIST_ATTR_NAME, allCategoriesList);
            sce.getServletContext().setAttribute(Constants.PRODUCER_LIST_ATTR_NAME, allProducersList);
        } catch (RuntimeException runtimeException) {
            LOGGER.error("Servlet context initialization failed: " + runtimeException.getMessage(), runtimeException);
            throw runtimeException;
        }
        LOGGER.info("Servlet context has been initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        serviceManager.close();
        LOGGER.info("The servlet context has been destroyed");
    }
}
