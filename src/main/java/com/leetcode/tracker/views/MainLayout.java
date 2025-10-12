package com.leetcode.tracker.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * Main layout for the application
 */
public class MainLayout extends AppLayout {
    
    public MainLayout() {
        createHeader();
        createDrawer();
    }
    
    private void createHeader() {
        H1 logo = new H1("LeetCode Memory Tracker");
        logo.addClassNames(
            LumoUtility.FontSize.LARGE,
            LumoUtility.Margin.MEDIUM
        );
        
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames(
            LumoUtility.Padding.Vertical.NONE,
            LumoUtility.Padding.Horizontal.MEDIUM
        );
        
        addToNavbar(header);
    }
    
    private void createDrawer() {
        SideNav nav = new SideNav();
        
        nav.addItem(new SideNavItem("Dashboard", DashboardView.class, VaadinIcon.DASHBOARD.create()));
        nav.addItem(new SideNavItem("Today's Review", TodayReviewView.class, VaadinIcon.CLOCK.create()));
        nav.addItem(new SideNavItem("Question List", QuestionListView.class, VaadinIcon.LIST.create()));
        nav.addItem(new SideNavItem("Statistics", StatisticsView.class, VaadinIcon.CHART.create()));
        
        addToDrawer(nav);
    }
}
