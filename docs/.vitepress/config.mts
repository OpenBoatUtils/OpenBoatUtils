import { defineConfig } from 'vitepress'

// https://vitepress.dev/reference/site-config
export default defineConfig({
  title: "OpenBoatUtils",
  description: "Configurable Boat Physics",
  themeConfig: {
    // https://vitepress.dev/reference/default-theme-config
    nav: [
      { text: 'Home', link: '/' },
      { text: 'Documentation', link: '/introduction' }
    ],

    sidebar: [
      {
        items: [
          { text: 'Introduction', link: '/introduction' },
          { text: 'Commands', link: '/commands' },
          { text: 'Modes', link: '/modes' }
        ]
      },
      {
        text: "Developers",
        items: [
         { text: 'Protocol', link: '/developers/protocol' },
         { text: 'Versions', link: '/developers/versions' },
         { text: 'Serverbound', link: '/developers/serverbound' },
         { text: 'Clientbound', link: '/developers/clientbound' },
        ]
     }
    ],

    logo: "/logo.png",

    socialLinks: [
      { icon: 'modrinth', link: 'https://modrinth.com/mod/openboatutils' },
      { icon: 'github', link: 'https://github.com/openboatutils/openboatutils' },
    ],

    editLink: {
      pattern: 'https://github.com/openboatutils/openboatutils/edit/main/docs/:path',
      text: 'Edit this page on GitHub'
    },

    search: {
      provider: 'local'
    },

    footer: {
      message: 'Released under the MIT License.'
    },

    lastUpdated: {
      text: 'Updated at',
      formatOptions: {
        dateStyle: 'full',
        timeStyle: 'medium'
      }
    }
  }
})
