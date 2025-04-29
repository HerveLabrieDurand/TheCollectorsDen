import { NgFor } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NgIcon, provideIcons } from '@ng-icons/core';
import {
  matEmojiEmotions,
  matForum,
  matGridView,
  matInsights,
  matLocalShipping,
  matPayment,
  matSearch,
  matSettings,
  matShoppingCart,
} from '@ng-icons/material-icons/baseline';
import { TranslatePipe } from '@ngx-translate/core';

@Component({
  selector: 'app-side-panel',
  imports: [RouterModule, NgIcon, NgFor, TranslatePipe],
  templateUrl: './side-panel.component.html',
  styleUrl: './side-panel.component.css',
  viewProviders: [
    provideIcons({
      matGridView,
      matSearch,
      matPayment,
      matShoppingCart,
      matEmojiEmotions,
      matLocalShipping,
      matInsights,
      matForum,
      matSettings,
    }),
  ],
})
export class SidePanelComponent {
  tabs = [
    {
      label: 'sidepanel.tabs.overview.label',
      icon: 'matGridView',
      route: '/overview',
    },
    {
      label: 'sidepanel.tabs.search.discover.label',
      icon: 'matSearch',
      route: '/search-discover',
    },
    {
      label: 'sidepanel.tabs.orders.payments.label',
      icon: 'matPayment',
      route: '/orders-payments',
    },
    {
      label: 'sidepanel.tabs.selling.bidding.label',
      icon: 'matShoppingCart',
      route: '/selling-bidding',
    },
    {
      label: 'sidepanel.tabs.reviews.ratings.label',
      icon: 'matEmojiEmotions',
      route: '/reviews-ratings',
    },
    {
      label: 'sidepanel.tabs.delivery.tracking.label',
      icon: 'matLocalShipping',
      route: '/delivery-tracking',
    },
    {
      label: 'sidepanel.tabs.activity.insights.label',
      icon: 'matInsights',
      route: '/activity-insights',
    },
    {
      label: 'sidepanel.tabs.community.label',
      icon: 'matForum',
      route: '/community',
    },
    {
      label: 'sidepanel.tabs.account.settings.label',
      icon: 'matSettings',
      route: '/account-settings',
    },
  ];
}
